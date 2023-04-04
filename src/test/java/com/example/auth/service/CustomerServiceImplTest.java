//package com.example.auth.service;
//
//import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
//import com.example.auth.commons.enums.Role;
//import com.example.auth.commons.exception.InvalidRequestException;
//import com.example.auth.commons.exception.NotFoundException;
//import com.example.auth.commons.service.AdminConfigurationService;
//import com.example.auth.commons.utils.JwtTokenUtil;
//import com.example.auth.commons.utils.PasswordUtils;
//import com.example.auth.commons.utils.Utils;
//import com.example.auth.decorator.pagination.CustomerFilter;
//import com.example.auth.decorator.pagination.CustomerSortBy;
//import com.example.auth.decorator.pagination.FilterSortRequest;
//import com.example.auth.decorator.pagination.Pagination;
//import com.example.auth.helper.CustomerServiceTestGenerator;
//import com.example.auth.model.Customer;
//import com.example.auth.repository.CustomerRepository;
//import com.example.auth.stockPile.repository.NotificationRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//import java.lang.reflect.InvocationTargetException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//public class CustomerServiceImplTest {
//    private static final String email = "sanskritishukla4@gmail.com";
//    private static final String id = "id";
//    private static final String otp = "234565";
//    private static final String password = "Sans@12345";
//    private static final String INVALID_OTP = "Invalid otp";
//    private static final String contact = "6386580393";
//
//
//    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
//    private final ModelMapper modelMapper = CustomerServiceTestGenerator.getModelMapper();
//    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
//    private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
//    private final PasswordUtils passwordUtils = mock(PasswordUtils.class);
//    private final AdminConfigurationService adminConfigurationService = mock(AdminConfigurationService.class);
//    private final Utils utils = mock(Utils.class);
//    private final NotificationRepository notificationRepository=mock(NotificationRepository.class);
//    public CustomerServiceImpl customerService = spy(new CustomerServiceImpl(customerRepository, modelMapper, passwordUtils, jwtTokenUtil, nullAwareBeanUtilsBean, adminConfigurationService, utils, notificationRepository));
//
//    @Test
//    void TestSignupUser() throws InvocationTargetException, IllegalAccessException {
//        //given
//        String password = customerService.password("Sans@12345");
//
//        var configuration = CustomerServiceTestGenerator.getMockAdminConfiguration();
//
//        var userModel = CustomerServiceTestGenerator.getMockuserModel(password);
//
//        var signUpAddRequest = CustomerServiceTestGenerator.getMockUserAddRequest(contact);
//
//        var signUpresponse = CustomerServiceTestGenerator.getMockSignUpResponse(password);
//
//        when(passwordUtils.encryptPassword("Sans@12345")).thenReturn(password);
//
//        when(adminConfigurationService.getConfiguration()).thenReturn(configuration);
//
//        when(customerRepository.save(userModel)).thenReturn(userModel);
//
//
//        //when
//        var actualData = customerService.addCustomer(signUpAddRequest, Role.USER);
//
//        //then
//        Assertions.assertEquals(signUpresponse, actualData);
//
//    }
//
//
//    @Test
//    void TestLogin() throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
//        //given
//        String otp = customerService.generateOtp();
//        var configuration = CustomerServiceTestGenerator.getMockAdminConfiguration();
//        String password = customerService.password("Sans@12345");
//
//        var userModel = CustomerServiceTestGenerator.getMockuserModel(password);
//
//        var loginAddRequst = CustomerServiceTestGenerator.getMockLoginRequest();
//
//        var signUpResponse = CustomerServiceTestGenerator.getMockLoginResponse(password, otp);
//
//        when(customerRepository.findUserByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.of(userModel));
//
//        when(adminConfigurationService.getConfiguration()).thenReturn(configuration);
//
//        when(customerService.generateOtp()).thenReturn(otp);
//
//        //when
//        var actualData = customerService.login(loginAddRequst);
//
//        //then
//        Assertions.assertEquals(signUpResponse, actualData);
//
//    }
//
//    @Test
//    void testCustomerById() {
//        //given
//        var customer = CustomerServiceTestGenerator.getMockuserModel(null);
//        var customerResponse = CustomerServiceTestGenerator.getMockSignUpResponse(null);
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(customer));
//
//        //when
//        var actualData = customerService.getCustomerById(id);
//
//        //then
//        Assertions.assertEquals(customerResponse, actualData);
//
//    }
//
//    @Test
//    void getAllCustomer() {
//        //given
//        String password = customerService.password("Sans@12345");
//
//        String otp = customerService.generateOtp();
//
//        var customer = CustomerServiceTestGenerator.getMockAllCustomer(otp, password);
//
//        var customerResponse = CustomerServiceTestGenerator.getMockAllCustomerResponse(password, otp);
//
//        when(customerRepository.findAllBySoftDeleteFalse()).thenReturn(customer);
//
//        when(customerService.generateOtp()).thenReturn(otp);
//
//        //when
//        var actualData = customerService.getAllCustomer();
//
//        //then
//        Assertions.assertEquals(customerResponse, actualData);
//    }
//
//
//    @Test
//    void testDeleteCustomer() {
//        //given
//        String password = customerService.password("Sans@12345");
//
//        var customer = CustomerServiceTestGenerator.getMockuserModel(password);
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(customer));
//
//        //when
//        customerService.deleteCustomer(id);
//
//        //then
//        verify(customerRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
//    }
//
//    @Test
//    void testOtpVerification() {
//        //given
//        String otp = customerService.generateOtp();
//        var customer = CustomerServiceTestGenerator.getMockuserModel(null);
//
//        when(customerRepository.existsByOtpAndEmailAndSoftDeleteIsFalse(otp, email)).thenReturn(true);
//
//        when(customerRepository.findUserByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(customer));
//
//        //when
//        customerService.otpVerification(otp, email);
//
//        //then
//        verify(customerRepository, times(1)).existsByOtpAndEmailAndSoftDeleteIsFalse(otp, email);
//    }
//
//
//    @Test
//    void testLogout() {
//        //given
//        var customer = CustomerServiceTestGenerator.getMockuserModel(null);
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(customer));
//
//        when(customerRepository.save(customer)).thenReturn(customer);
//
//        //when
//        customerService.logout(id);
//
//        //then
//        verify(customerRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
//    }
//
//    @Test
//    void testForgetPassword() {
//        //given
//        String otp = customerService.generateOtp();
//        var customer = CustomerServiceTestGenerator.getMockuserModel(null);
//
//        var emailModel = CustomerServiceTestGenerator.getMockEmailModel(otp);
//
//        when(customerRepository.findUserByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(customer));
//
//        utils.sendEmailNow(emailModel);
//
//        when(customerRepository.save(customer)).thenReturn(customer);
//
//        //when
//        customerService.forgetPassword(email);
//
//        //then
//        verify(customerRepository, times(1)).findUserByEmailAndSoftDeleteIsFalse(email);
//
//    }
//
//
//    @Test
//    void testSetPassword() {
//        //given
//        String password = customerService.passwords("Sans@12345");
//
//        var csutomer = CustomerServiceTestGenerator.getMockuserModel(password);
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(csutomer));
//
//        when(customerRepository.save(csutomer)).thenReturn(csutomer);
//
//        //when
//        customerService.setPassword(password, password, id);
//
//        //then
//        verify(customerRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
//
//    }
//    @Test
//    void testGetAllCustomerByPagination() {
//        //given
//        CustomerFilter customerFilter = new CustomerFilter();
//
//        customerFilter.setId(customerFilter.getId());
//
//        FilterSortRequest.SortRequest<CustomerSortBy> sort = new FilterSortRequest.SortRequest<>();
//
//        sort.setOrderBy(Sort.Direction.ASC);
//
//        sort.setSortBy(CustomerSortBy.NAME);
//
//        Pagination pagination = new Pagination();
//
//        pagination.setPage(1);
//        pagination.setLimit(5);
//
//        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
//
//        var customer = CustomerServiceTestGenerator.getMockAllCustomer(null, null);
//
//        Page<Customer> page = new PageImpl<>(customer);
//
//        when(customerRepository.getAllCustomerByPagination(customerFilter, sort, pageRequest)).thenReturn(page);
//
//        //when
//        var actualData = customerService.getAllCustomerByPagination(customerFilter, sort, pageRequest);
//
//        //then
//        Assertions.assertEquals(page, actualData);
//
//    }
//
//    @Test
//    void testPasswordNotMatched() {
//        String password = customerService.passwords("Sans@12345");
//
//        var csutomer = CustomerServiceTestGenerator.getMockuserModel(password);
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(csutomer));
//
//        when(customerRepository.save(csutomer)).thenReturn(csutomer);
//
//
//        //when
//        Throwable exception = assertThrows(NotFoundException.class, () -> customerService.setPassword(password, "sans", id));
//
//        //then
//        Assertions.assertEquals("Password not matched", exception.getMessage());
//    }
//
//
//    @Test
//    void testInvalidPhoneNumber() throws InvocationTargetException, IllegalAccessException {
//        //given
//        var customer = CustomerServiceTestGenerator.getMockuserModel(null);
//
//        var signUpAddRequest = CustomerServiceTestGenerator.getMockUserAddRequest("638658039");
//
//
//        var configuration = CustomerServiceTestGenerator.getMockAdminConfiguration();
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(customer));
//
//        when(adminConfigurationService.getConfiguration()).thenReturn(configuration);
//
//        //when
//
//        Throwable excption = assertThrows(InvalidRequestException.class, () -> customerService.addCustomer(signUpAddRequest, Role.USER));
//
//        //then
//        Assertions.assertEquals("Invalid phone number", excption.getMessage());
//    }
//
//    @Test
//    void testInvalidOtp() {
//        //given
//        String otp = customerService.generateOtp();
//
//        var customer = CustomerServiceTestGenerator.getMockuserModel(null);
//
//        when(customerRepository.existsByOtpAndEmailAndSoftDeleteIsFalse(otp, email)).thenReturn(true);
//
//        when(customerRepository.findUserByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(customer));
//
//        //when
//        Throwable exception = assertThrows(InvalidRequestException.class, () -> customerService.otpVerification("343565", email));
//
//        //then
//        Assertions.assertEquals(INVALID_OTP, exception.getMessage());
//
//    }
//
//
//
//
//
//
//
//
//}