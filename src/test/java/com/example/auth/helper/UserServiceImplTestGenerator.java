package com.example.auth.helper;

import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.UserExcelResponse;
import com.example.auth.decorator.user.*;
import com.example.auth.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;

public class UserServiceImplTestGenerator {
    private final static String id = "id";

    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public static UsersAddRequest getMockUserAddRequest() {
        return UsersAddRequest.builder()
                .firstName("sans")
                .middleName("km")
                .lastName("shukla")
                .occupation("java developer")
                .email("sanskrityshukla4@gmail.com")
                .age(21)
                .phoneNumber("6386580393")
                .salary(25000)
                .build();
    }

    public static UserResponse getMockUserResponse(String token,String fullName) {
        return UserResponse.builder()
                .firstName("sans")
                .middleName("km")
                .lastName("shukla")
                .fullName(fullName)
                .occupation("java developer")
                .email("sanskrityshukla4@gmail.com")
                .age(21)
                .token(token)
                .phoneNumber("6386580393")
                .salary(25000)
                .build();

    }

    public static User getMockUser(Role role) {
        return User.builder()
                .firstName("sans")
                .middleName("km")
                .lastName("shukla")
                .fullName("sans km shukla")
                .occupation("java developer")
                .email("sanskrityshukla4@gmail.com")
                .age(21)
                .role(role)
                .phoneNumber("6386580393")
                .salary(25000)
                .softDelete(false)
                .build();
    }

    public static List<User> getMockUsers() {
        return List.of(User.builder()
                .firstName("sans")
                .middleName("km")
                .lastName("shukla")
                .occupation("java developer")
                .email("sanskrityshukla4@gmail.com")
                .softDelete(false)
                .build());
    }

    public static List<UserResponse> getMockResponse() {
        return List.of(UserResponse.builder()
                .firstName("sans")
                .middleName("km")
                .lastName("shukla")
                .occupation("java developer")
                .email("sanskrityshukla4@gmail.com")
                .build());
    }

    public static UserFilter getMockUserFilter() {
        return UserFilter.builder()
                .age(23)
                .name("sans")
                .build();
    }

    public static List<UserAggregationResponse> getMockAggregationFilter() {
        return List.of(UserAggregationResponse.builder()
                .count(5)
                .name("sans")
                .build());
    }

    public static UserResponse getResponse(Role role) {
        return UserResponse.builder().role(role).build();
    }

    public static List<UserExcelResponse> getExcelResponse() {
        return List.of(UserExcelResponse.builder()
                .age(23)
                .firstName("sans")
                .middleName("km")
                .lastName("shukla")
                .fullName("sans km shukla")
                .age(21)
                .occupation("java developer")
                .salary(25000)
                .build());
    }


    public static List<UserSpiResponse> getSpiResponse(){
        return List.of(UserSpiResponse.builder()
                ._id(id)
                .auth(getSpiData())
                .build());
    }

     public static List<UserSpiData> getSpiData(){
        return List.of(UserSpiData.builder()
                ._id(id)
                 .firstName("krish")
                .email("krish@gmail.com")
                .semester("7")
                .build());

    }
    public static UserSpiResponse getSpiResponseInExcel(){
        return UserSpiResponse.builder()
                .build();
    }

}

