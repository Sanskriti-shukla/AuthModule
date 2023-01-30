package com.example.auth.repository;

import com.example.auth.decorator.UserDetailsExcelResponse;
import com.example.auth.decorator.pagination.UserFilterData;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.UserSortBy;

import com.example.auth.decorator.user.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserCustomRepository {
 List<UserResponse> getByFilterAndSoftDeleteFalse(UserFilter userFilter)  ;
List<UserAggregationResponse>  getUserByAggregation(UserFilter userFilter);
 public List<UserSpiResponse> getUserBySpi(double spi );
 public Page<UserResponse> getAllUserByPagination(UserFilterData filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pageRequest);
 public Page<UserDetailsExcelResponse> getAllUserByPaginationInExcel(UserFilterData filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException;
 public List<MaxSpiResponse> getUserByMaxSpi(String id);
}
