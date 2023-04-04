package com.example.auth.stockPile.service;


import com.example.auth.stockPile.decorator.UserAddRequest;
import com.example.auth.stockPile.decorator.UserDataResponse;

import java.util.List;


public interface UserDataService {

    UserDataResponse addUser(UserAddRequest userAddRequest);

    void updateUser(UserAddRequest userAddRequest, String id) throws NoSuchFieldException, IllegalAccessException;

    void deleteUser(String id);

    UserDataResponse getUserById(String id);

    List<UserDataResponse> getAllUser();


    String getUserIdByEmail(String email);


}
