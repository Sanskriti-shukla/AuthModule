package com.example.auth.service;

import com.example.auth.commons.FileLoader;
import com.example.auth.commons.JWTUser;
import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.decorator.TemplateParser;
import com.example.auth.commons.enums.PasswordEncryptionType;
import com.example.auth.commons.enums.Role;
import com.example.auth.commons.exception.InvalidRequestException;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.model.EmailModel;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.commons.utils.JwtTokenUtil;
import com.example.auth.commons.utils.PasswordUtils;
import com.example.auth.commons.utils.Utils;
import com.example.auth.decorator.ImageUrl;
import com.example.auth.decorator.SocialVerify;
import com.example.auth.decorator.customer.*;
import com.example.auth.decorator.pagination.CustomerFilter;
import com.example.auth.decorator.pagination.CustomerSortBy;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.model.Customer;
import com.example.auth.repository.CustomerRepository;
import com.example.auth.stockPile.decorator.NotificationAddRequest;
import com.example.auth.stockPile.model.Notification;
import com.example.auth.stockPile.model.ServiceType;
import com.example.auth.stockPile.model.UserData;
import com.example.auth.stockPile.repository.NotificationRepository;
import com.example.auth.stockPile.repository.UserDataRepository;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private static final long OTP_VALID_DURATION = 1 * 60 * 1000;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordUtils passwordUtils;
    private final JwtTokenUtil jwtTokenUtil;
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    private final AdminConfigurationService adminConfigurationService;
    private final Utils utils;
    private final NotificationRepository notificationRepository;
    private final UserDataRepository userDataRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, PasswordUtils passwordUtils, JwtTokenUtil jwtTokenUtil, NullAwareBeanUtilsBean nullAwareBeanUtilsBean, AdminConfigurationService adminConfigurationService, Utils utils, NotificationRepository notificationRepository, UserDataRepository userDataRepository) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.passwordUtils = passwordUtils;
        this.jwtTokenUtil = jwtTokenUtil;
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.adminConfigurationService = adminConfigurationService;
        this.utils = utils;
        this.notificationRepository = notificationRepository;

        this.userDataRepository = userDataRepository;
    }


    @Override
    public CustomerResponse addCustomer(CustomerAddRequest customerAddRequest, Role role, ServiceType serviceType) {
        checkValidation(customerAddRequest);
        if (!serviceType.name().equals("NORMAL")) {
            return null;
        }
        adminConfigurationService.getConfiguration();
        Customer newCustomer = modelMapper.map(customerAddRequest, Customer.class);
        updatePassword(newCustomer);
        newCustomer.setRole(role);
        newCustomer.setDate(new Date());
        sendMail(newCustomer);
        customerRepository.save(newCustomer);
        CustomerResponse userResponse = modelMapper.map(newCustomer, CustomerResponse.class);
        return userResponse;
    }

    private void updatePassword(Customer customer) {
        if (customer.getPassword() != null) {
            String hashedPassword = password(customer.getPassword());
            customer.setPassword(hashedPassword);
        }
    }


    @VisibleForTesting
    public String password(String password) {
        return PasswordUtils.encryptPassword(password);
    }


    @Override
    public CustomerResponse login(CustomerLoginAddRequest customerLoginAddRequest) {

        Customer customer = getUserByEmail(customerLoginAddRequest.getEmail());
        String userPassworod = customer.getPassword();
        CustomerResponse customerResponse = modelMapper.map(customer, CustomerResponse.class);
        boolean passwords = false;
        try {
            passwords = passwordUtils.isPasswordAuthenticated(customerLoginAddRequest.getPassword(), userPassworod, PasswordEncryptionType.BCRYPT);
        } catch (NoSuchAlgorithmException e) {
            log.error("error occured during passwordAuthentication : {}", e.getMessage(), e);
        }
        if (passwords) {
            modelMapper.map(customerResponse, Customer.class);
        } else {
            throw new InvalidRequestException(MessageConstant.INCORRECT_PASSWORD);
        }
        customer.setDate(new Date());
        customer.setOtpSendtime(new Date());
        customer.setLoginTime(new Date());
        customer.setLogin(true);
        sendMail(customer);
        JWTUser jwtUser = new JWTUser(customerLoginAddRequest.getEmail(), Collections.singletonList(customerResponse.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        customerResponse.setToken(token);
        customerRepository.save(customer);
        return customerResponse;

    }
    public void sendMail(Customer customer) {
        AdminConfiguration adminConfiguration = adminConfigurationService.getConfiguration();
        String otp = generateOtp();
        EmailModel emailModel = new EmailModel();
        emailModel.setMessage(otp);
        customer.setOtp(otp);
        TemplateParser<EmailModel> templateParser = new TemplateParser<>();
        String url = templateParser.compileTemplate(FileLoader.loadHtmlTemplateOrReturnNull("send_otp"), emailModel);
        emailModel.setMessage(url);
        emailModel.setTo("nilusroy0@gmail.com");
        emailModel.setCc(adminConfiguration.getTechAdmins());
        emailModel.setSubject("OTP Verification");
        utils.sendEmailNow(emailModel);

    }


    @VisibleForTesting
    public String generateOtp() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        String otp = String.format("%06d", number);
        return otp;
    }


    @Override
    public Page<Customer> getAllCustomerByPagination(CustomerFilter filter, FilterSortRequest.SortRequest<CustomerSortBy> sort, PageRequest pagination) {
        return customerRepository.getAllCustomerByPagination(filter, sort, pagination);
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = getById(id);
        CustomerResponse customerResponse = modelMapper.map(customer, CustomerResponse.class);
        return customerResponse;
    }

    @Override
    public List<CustomerResponse> getAllCustomer() {
        List<Customer> customerResponses = customerRepository.findAllBySoftDeleteFalse();

        List<CustomerResponse> list = new ArrayList<>();
        customerResponses.forEach(customer -> {
            CustomerResponse customerResponse = modelMapper.map(customer, CustomerResponse.class);
            list.add(customerResponse);
        });

        return list;
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer = getById(id);
        customer.setSoftDelete(true);
        customerRepository.save(customer);

    }


    @Override
    public void otpVerification(String otp, String email) {
        boolean customer1 = customerRepository.existsByOtpAndEmailAndSoftDeleteIsFalse(otp, email);
        if (customer1) {
            Customer customer = getUserByEmail(email);
            if (customer.getOtpSendtime().getTime() + OTP_VALID_DURATION < System.currentTimeMillis()) {
                throw new InvalidRequestException(MessageConstant.OTP_EXPIRED);
            }
        } else {
            throw new InvalidRequestException(MessageConstant.INVAILD_OTP);
        }

    }

    @Override
    public void logout(String id) {
        Customer customer = getById(id);
        customer.setLogin(false);
        customer.setLogoutTime(new Date());
        customerRepository.save(customer);
    }

    @Override
    public void forgetPassword(String email) {
        Customer customer = getUserByEmail(email);
        String otp = generateOtp();
        EmailModel emailModel = new EmailModel();
        emailModel.setMessage(otp);
        emailModel.setTo("sanskriti.s@techroversolutions.com");
        emailModel.setSubject("OTP Verification");
        utils.sendEmailNow(emailModel);
        customer.setOtp(otp);
        customerRepository.save(customer);

    }

    @Override
    public void setPassword(String newPassword, String confirmPassword, String id) {
        if (newPassword.equals(confirmPassword)) {
            Customer customer = getById(id);
            customer.setPassword(passwords(confirmPassword));
            customerRepository.save(customer);
        } else {
            throw new NotFoundException(MessageConstant.PASSWORD_NOT_MATCHED);
        }

    }


    @Override
    public String getEncryptPassword(String id) {

        Customer customer = getById(id);
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setName(customer.getName());

        customerResponse.setPassword(customer.getPassword());
        if (customer.getPassword() != null) {
            String password = PasswordUtils.encryptPassword(customer.getPassword());
            customer.setPassword(password);
            customerRepository.save(customer);
            return password;
        } else {
            throw new NotFoundException(MessageConstant.PASSWORD_EMPTY);
        }
    }

    @Override
    public String getIdFromToken(String token) {

        String id = jwtTokenUtil.getCustomerIdFromToken(token);
        boolean exist = customerRepository.existsById(id);

        if (exist) {
            return id;
        } else {
            throw new InvalidRequestException(MessageConstant.INVALID_TOKEN);
        }
    }

    @Override
    public CustomerResponse getToken(String id) {
        Customer customer = getById(id);
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setRole(customer.getRole());
        JWTUser jwtUser = new JWTUser(id, Collections.singletonList(customerResponse.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);

        try {
            nullAwareBeanUtilsBean.copyProperties(customerResponse, customer);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        customerResponse.setToken(token);
        return customerResponse;
    }

    @Override
    public void addDeviceToken(NotificationAddRequest notificationAddRequest) {
        Notification notification = modelMapper.map(notificationAddRequest, Notification.class);
        Notification existingNotification = getUserId(notification.getUserId());
        if (existingNotification == null) {
            notificationRepository.save(notification);
        } else if (notificationAddRequest.getUserId().equals(existingNotification.getUserId()) && notificationAddRequest.getDeviceToken().equals(existingNotification.getDeviceToken())) {
            throw new InvalidRequestException(MessageConstant.DEVICE_TOKEN_ALREDAY_EXIST);
        } else {
            // If the same user ID is present in the database, update the device token
            existingNotification.setDeviceToken(notification.getDeviceToken());
            notificationRepository.save(existingNotification);
        }
    }


    @Override
    public void deleteDeviceToken(String userId) {
        Notification notification = getUserId(userId);
        if (notification != null) {
            notification.setDeviceToken("");
            notificationRepository.save(notification);
        }
    }

    @Override
    public SocialVerificationData socialVerification(SocialVerificationAddRequest socialVerificationAddRequest, SocialVerify socialVerify) {
        Customer customer = getUserByEmail(socialVerificationAddRequest.getEmail());
        UserData userData = userDataRepository.findByEmail(socialVerificationAddRequest.getEmail());
        SocialVerificationData socialVerificationData = modelMapper.map(customer, SocialVerificationData.class);
        Map<SocialVerify, Boolean> socialVerifyMap = userData.getSocialVerify();
        socialVerifyMap.put(socialVerify, true);
        Map<ImageUrl, String> imageUrl = userData.getImageUrl();
        if (socialVerify == SocialVerify.GOOGLE) {
            imageUrl.put(ImageUrl.GOOGLE_IMG_URL, socialVerificationAddRequest.getImageUrl());
            userData.setImageUrl(imageUrl);
            customer.setImageUrl(imageUrl);
        } else if (socialVerify == SocialVerify.FACEBOOK) {
            imageUrl.put(ImageUrl.FACEBOOK_IMG_URL, socialVerificationAddRequest.getImageUrl());
            userData.setImageUrl(imageUrl);
            customer.setImageUrl(imageUrl);
        }

        socialVerifyMap = customer.getSocialVerify();
        socialVerifyMap.put(socialVerify, true);

        // Save the Customer and UserData objects
        userDataRepository.save(userData);
        customerRepository.save(customer);

        socialVerificationData.setImageUrl(customer.getImageUrl());
        socialVerificationData.setSocialVerify(customer.getSocialVerify());

        return socialVerificationData;
    }

    @VisibleForTesting
    public String passwords(String confirmPassword) {
        return passwordUtils.encryptPassword(confirmPassword);
    }


    public void checkValidation(CustomerAddRequest customerAddRequest) {
        AdminConfiguration adminConfiguration = adminConfigurationService.getConfiguration();
        if (!customerAddRequest.getPassword().equals(customerAddRequest.getConfirmPassword())) {
            throw new InvalidRequestException(MessageConstant.INCORRECT_PASSWORD);
        }
        if (!customerAddRequest.getContact().matches(adminConfiguration.getMobileNoRegex())) {
            throw new InvalidRequestException(MessageConstant.INVALID_PHONE_NUMBER);
        }
        if (!customerAddRequest.getName().matches(adminConfiguration.getNameRegex())) {
            throw new InvalidRequestException(MessageConstant.NAME_MUST_NOT_BE_NULL);
        }
        if (!customerAddRequest.getUserName().matches(adminConfiguration.getNameRegex())) {
            throw new InvalidRequestException(MessageConstant.USERNAME_MUST_NOT_BE_NULL);
        }
        if (customerRepository.existsByEmailAndSoftDeleteIsFalse(customerAddRequest.getEmail())) {
            throw new InvalidRequestException(MessageConstant.EMAIL_ALREADY_EXIST);
        }
        if (!customerAddRequest.getEmail().matches(adminConfiguration.getEmailRegex())) {
            throw new InvalidRequestException(MessageConstant.INVALID_EMAIL);
        }

        if (!customerAddRequest.getPassword().matches(adminConfiguration.getPasswordRegex())) {
            throw new InvalidRequestException(MessageConstant.WRONG_PASSWORD_FORMAT);
        }
    }

    public Customer getUserByEmail(String email) {
        return customerRepository.findUserByEmailAndSoftDeleteIsFalse(email).orElseThrow(() -> new NotFoundException(MessageConstant.EMAIL_NOT_FOUND));
    }

    public Customer getById(String id) {
        return customerRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));

    }
    public Notification getUserId(String userId) {
        return notificationRepository.findByUserId(userId);
    }
}

