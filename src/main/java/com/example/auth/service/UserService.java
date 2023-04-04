package com.example.auth.service;

import com.example.auth.decorator.MonthAndYear;
import com.example.auth.decorator.UserEligibilityAggregation;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.UserFilterData;
import com.example.auth.decorator.pagination.UserSortBy;
import com.example.auth.decorator.user.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public interface UserService {
    UserResponse addUser(UsersAddRequest userAddRequest);

    List<UserResponse> getAllUser();

    UserResponse getUser(String id) ;

    void deleteUser(String id);

    List<UserResponse> getUserByAge(UserFilter userFilter);

    List<UserAggregationResponse> getUserBySalary(UserFilter userFilter);

    Page<UserResponse> getAllUserByPagination(UserFilterData filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pageRequest);

    List<MaxSpiResponse> getUserByMaxSpi(String id);

    UserResponse getToken(String id) ;

    String getIdFromToken(String token);

    Date getExpirationDateFromToken(String token);

    boolean isTokenExpired(String token);

    Workbook getAllUserInExcel();

    Workbook getUserDetailsByResultSpi(UserFilterData filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pageRequest) ;


    Page<UserEligibilityAggregation> getUserEligibilityByAge(UserFilterData filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) throws JSONException;

    void updateUser(String id, UsersAddRequest userAddRequest);


    MonthAndYear userChartApi(int year);


}


