package com.example.auth.helper;

import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.customer.CustomerLoginAddRequest;
import com.example.auth.decorator.customer.CustomerAddRequest;
import com.example.auth.decorator.customer.CustomerResponse;
import com.example.auth.model.Customer;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class CustomerServiceTestGenerator {

    private static final String email = "sanskritishukla4@gmail.com";

    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public static Customer getMockuserModel(String passwords) {
        return Customer.builder()
                .name("sans")
                .email(email)
                .password(passwords)
                .role(Role.USER)
                .userName("sans shukla")
                .contact("33454545646")
                .build();
    }

    public static CustomerAddRequest getMockUserAddRequest() {
        return CustomerAddRequest.builder()
                .email(email)
                .password("password")
                .contact("6386580393")
                .userName("sans shukla")
                .confirmPassword("password")
                .name("sans")
                .build();

    }

    public static CustomerResponse getMockSignUpResponse(String password) {
        return CustomerResponse.builder()
                .email(email)
                .contact("6386580393")
                .password(password)
                .name("sans")
                .userName("sans shukla")
                .role(Role.USER)
                .build();
    }

    public static CustomerLoginAddRequest getMockLoginRequest() {
        return CustomerLoginAddRequest.builder()
                .email(email)
                .password("password")
                .build();
    }

    public static CustomerResponse getMockLoginResponse(String password) {
        return CustomerResponse.builder()
                .password(password)
                .email(email)
                .role(Role.USER)
                .name("sans")
                .userName("sans shukla")
                .contact("33454545646")
                .build();
    }

}
