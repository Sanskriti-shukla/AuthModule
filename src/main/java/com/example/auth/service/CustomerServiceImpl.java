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
import com.example.auth.decorator.customer.CustomerAddRequest;
import com.example.auth.decorator.customer.CustomerLoginAddRequest;
import com.example.auth.decorator.customer.CustomerResponse;
import com.example.auth.decorator.pagination.CustomerFilter;
import com.example.auth.decorator.pagination.CustomerSortBy;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.model.Customer;
import com.example.auth.repository.CustomerRepository;
import com.example.auth.stockPile.decorator.NotificationAddRequest;
import com.example.auth.stockPile.model.Notification;
import com.example.auth.stockPile.model.UserData;
import com.example.auth.stockPile.repository.NotificationRepository;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
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

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, PasswordUtils passwordUtils, JwtTokenUtil jwtTokenUtil, NullAwareBeanUtilsBean nullAwareBeanUtilsBean, AdminConfigurationService adminConfigurationService, Utils utils, NotificationRepository notificationRepository) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.passwordUtils = passwordUtils;
        this.jwtTokenUtil = jwtTokenUtil;
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.adminConfigurationService = adminConfigurationService;
        this.utils = utils;
        this.notificationRepository = notificationRepository;
    }


    @Override
    public CustomerResponse addCustomer(CustomerAddRequest customerAddRequest, Role role) {
        AdminConfiguration adminConfiguration = adminConfigurationService.getConfiguration();
        Customer signUpUser1 = modelMapper.map(customerAddRequest, Customer.class);
        CustomerResponse userResponse1 = modelMapper.map(customerAddRequest, CustomerResponse.class);
        if (signUpUser1.getPassword() != null) {
            String password = password(signUpUser1.getPassword());
            signUpUser1.setPassword(password);
            userResponse1.setPassword(password);
        }
        checkValidation(customerAddRequest);
        signUpUser1.setRole(role);
        userResponse1.setRole(role);
        signUpUser1.setDate(new Date());
        String otp = generateOtp();
        signUpUser1.setOtp(otp);
        sendMail();
        customerRepository.save(signUpUser1);
        return userResponse1;
    }

    @VisibleForTesting
    public String password(String password) {
        return PasswordUtils.encryptPassword(password);
    }


    @Override
    public CustomerResponse login(CustomerLoginAddRequest customerLoginAddRequest) {

        AdminConfiguration adminConfiguration = adminConfigurationService.getConfiguration();

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
        customerResponse.setRole(customer.getRole());
        customerResponse.setId(customer.getId());
        JWTUser jwtUser = new JWTUser(customerLoginAddRequest.getEmail(), Collections.singletonList(customerResponse.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);

        try {
            nullAwareBeanUtilsBean.copyProperties(customerResponse, customer);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("error occured when mapping model to dto : {}", e.getMessage(), e);
        }

        customerResponse.setToken(token);
        customer.setDate(new Date());
        customer.setOtpSendtime(new Date());
        customer.setLoginTime(new Date());
        String otp = generateOtp();
        customer.setOtp(otp);
        customer.setLogin(true);
        customerResponse.setOtp(customer.getOtp());
        customerRepository.save(customer);
        return customerResponse;

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
        emailModel.setTo("jash.g@techroversolutions.com");
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

    public void sendMail() {
        AdminConfiguration adminConfiguration = adminConfigurationService.getConfiguration();
        String otp = generateOtp();
        EmailModel emailModel = new EmailModel();
        emailModel.setMessage(otp);
        TemplateParser<EmailModel> templateParser = new TemplateParser<>();
        String url = templateParser.compileTemplate(FileLoader.loadHtmlTemplateOrReturnNull("send_otp"), emailModel);
        emailModel.setMessage(url);
        emailModel.setTo("roynilus@outlook.com");
        emailModel.setCc(adminConfiguration.getTechAdmins());
        emailModel.setSubject("OTP Verification");
        utils.sendEmailNow(emailModel);

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

//    @Override
//    public void addDeviceToken(NotificationAddRequest notificationAddRequest) {
//        Notification notification=modelMapper.map(notificationAddRequest,Notification.class);
//        Notification existingNotification = getUserId(notification.getUserId());
//
//            if (existingNotification != null) {
//                // If the same user ID is present in the database, update the device token
//                existingNotification.setDeviceToken(notification.getDeviceToken());
//                notificationRepository.save(existingNotification);
//            } else {
//                // If the user ID is not present in the database, save the new notification
//                notificationRepository.save(notification);
//            }
//        }
@Override
public void addDeviceToken(NotificationAddRequest notificationAddRequest) {
    if (notificationAddRequest.getUserId() == null) {
        throw new IllegalArgumentException("User ID cannot be null");
    }
    Notification notification = modelMapper.map(notificationAddRequest, Notification.class);
    Notification existingNotification = getUserId(notification.getUserId());
    if (existingNotification != null) {
        // If the same user ID is present in the database, update the device token
        existingNotification.setDeviceToken(notification.getDeviceToken());
        notificationRepository.save(existingNotification);
    } else {
        // If the user ID is not present in the database, save the new notification
        notificationRepository.save(notification);
    }
}



    @Override
    public void deleteDeviceToken(String userId) {
     Notification notification=getUserId(userId);
            if (notification != null) {
                notification.setDeviceToken("");
                notificationRepository.save(notification);
            }
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
        return customerRepository.findUserByEmailAndSoftDeleteIsFalse(email).orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    }

    public Customer getById(String id) {
        return customerRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));

    }

    public Notification getUserId(String userId){
        return notificationRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    }

}

