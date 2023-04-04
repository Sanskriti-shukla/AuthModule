package com.example.auth.service;

import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.SocialVerify;
import com.example.auth.decorator.customer.*;
import com.example.auth.decorator.pagination.CustomerFilter;
import com.example.auth.decorator.pagination.CustomerSortBy;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.model.Customer;
import com.example.auth.stockPile.decorator.NotificationAddRequest;
import com.example.auth.stockPile.model.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface CustomerService {

    CustomerResponse addCustomer(CustomerAddRequest signUpAddRequest, Role role, ServiceType serviceType) ;

    CustomerResponse login(CustomerLoginAddRequest customerLoginAddRequest) ;

    Page<Customer> getAllCustomerByPagination(CustomerFilter filter, FilterSortRequest.SortRequest<CustomerSortBy> sort, PageRequest pagination);

    CustomerResponse getCustomerById(String id);

    List<CustomerResponse> getAllCustomer();

    void deleteCustomer(String id);

    void otpVerification(String otp, String email);


    void logout(String id);


    void forgetPassword(String email);

    void setPassword(String newPassword, String confirmPassword, String id);

    String getEncryptPassword(String id);


    String getIdFromToken(String token);


    CustomerResponse getToken(String id) throws InvocationTargetException, IllegalAccessException;

    void addDeviceToken(NotificationAddRequest notificationAddRequest);

    void deleteDeviceToken(String userId);

    SocialVerificationData socialVerification(SocialVerificationAddRequest socialVerificationAddRequest, SocialVerify socialVerify);

    void updateCustomer(CustomerAddRequest customerAddRequest, Role role, ServiceType serviceType, String customerId);

//    void sendOtp(String email, String otp);
}


