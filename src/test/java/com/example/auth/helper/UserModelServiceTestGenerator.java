package com.example.auth.helper;

import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.customer.CustomerAddRequest;
import com.example.auth.decorator.customer.CustomerSignupAddRequest;
import com.example.auth.decorator.customer.CustomerSignupResponse;
import com.example.auth.model.UserModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class UserModelServiceTestGenerator {

    private static final String email = "sans@1234gmail.com";

    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public static UserModel getMockuserModel(String passwords) {
        return UserModel.builder()
                .name("sans")
                .email(email)
                .password(passwords)
                .role(Role.USER)
                .userName("sans shukla")
                .contact("33454545646")
                .build();
    }

    public static CustomerSignupAddRequest getMockUserAddRequest() {
        return CustomerSignupAddRequest.builder()
                .email(email)
                .password("password")
                .confirmPassword("password")
                .name("sans")
                .build();

    }

    public static CustomerSignupResponse getMockSignUpResponse(String password) {
        return CustomerSignupResponse.builder()
                .email(email)
                .password(password)
                .name("sans")
                .role(Role.USER)
                .build();
    }

    public static CustomerAddRequest getMockLoginRequest() {
        return CustomerAddRequest.builder()
                .email(email)
                .password("password")
                .build();
    }

    public static CustomerSignupResponse getMockLoginResponse() {
        return CustomerSignupResponse.builder()
                .password("password")
                .email(email)
                .build();
    }

}