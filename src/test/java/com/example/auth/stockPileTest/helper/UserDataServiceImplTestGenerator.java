package com.example.auth.stockPileTest.helper;


import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.stockPile.decorator.UserAddRequest;
import com.example.auth.stockPile.decorator.UserDataResponse;
import com.example.auth.stockPile.model.Subscribe;
import com.example.auth.stockPile.model.UserData;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;

public  class UserDataServiceImplTestGenerator {
    private final static String userId="id";
        public static final Subscribe subscribe= Subscribe.SUBSCRIBE;
    private final AdminConfiguration adminConfiguration= Mockito.mock(AdminConfiguration.class);

    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
    public static UserData mockUserData(){
        return UserData.builder()
                .subscribe(true)
                .id(userId)
                .email("sans@gmail.com")
                .userName("sans")
                .name("sans")
                .contact("6385254154")
                .build();
    }

    public static UserDataResponse mockUserDataResponse(String id){
        return UserDataResponse.builder()
                .id(id)
                .email("sans@gmail.com")
                .userName("sans")
                .name("sans")
                .contact("6385254154")
                .build();

    }

    public static UserAddRequest mockUserAddRequest(){
        return UserAddRequest.builder()
                .email("sans@gmail.com")
                .userName("sans")
                .name("sans")
                .contact("6385254154")
                .build();
    }
    public static List<UserDataResponse> mockListOfUserDataResponse() {
        return List.of(UserDataResponse.builder()
                .id(userId)
                .email("sans@gmail.com")
                .userName("sans")
                .name("sans")
                .contact("6385254154")
                .build());
    }

    public static List<UserData> mockAllUserData(){
        return List.of( UserData.builder()
                .subscribe(true)
                .id(userId)
                .email("sans@gmail.com")
                .userName("sans")
                .name("sans")
                .contact("6385254154")
                .build());
    }

    public static AdminConfiguration getMockAdminConfiguration() {
        return AdminConfiguration.builder()
                .mobileNoRegex("^[0-9]{10}$")
                .nameRegex("^[a-zA-Z]+$")
                .passwordRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,15}$")
                .emailRegex("^[A-Za-z0-9+_.-]+@(.+)$")
                .username("^[a-zA-Z]+$")
                .build();
    }


}
