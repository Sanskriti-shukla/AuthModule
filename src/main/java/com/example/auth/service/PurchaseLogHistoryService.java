package com.example.auth.service;

import com.example.auth.decorator.*;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.PurchaseLogFilter;
import com.example.auth.decorator.pagination.PurchaseLogSortBy;
import com.example.auth.model.PurchaseLogHistory;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface PurchaseLogHistoryService {

    PurchaseLogHistoryResponse addPurchaseLog(PurchaseLogHistoryAddRequest purchaseLogHistoryAddRequest, String customerId,String itemName);

    void updatePurchaseLog(PurchaseLogHistoryAddRequest purchaseLogHistoryAddRequest, String id) ;

    PurchaseLogHistoryResponse getPurchaseLogById(String id);

    List<PurchaseLogHistoryResponse> getAllPurchaseLog();


    void deletePurchaseLogById(String id);

    Page<PurchaseLogHistoryResponse> getAllPurchaseLogByPagination(PurchaseLogFilter purchaseLogFilter, FilterSortRequest.SortRequest<PurchaseLogSortBy> sort, PageRequest pageRequest);


    List<PurchaseLogHistory> findById(String customerId);


    void save(MultipartFile file);


    List<PurchaseAggregationResponse> getItemPurchaseDetailsByMonthYear() throws JSONException;

    List<ItemPurchaseAggregationResponse> getPurchaseDetailsByCustomerName();


    Workbook getPurchaseLogByMonth(PurchaseLogFilter filter, FilterSortRequest.SortRequest<PurchaseLogSortBy> sort, PageRequest pagination) ;


    Workbook getPurchaseDetailsByCustomer(PurchaseLogFilter filter, FilterSortRequest.SortRequest<PurchaseLogSortBy> sort, PageRequest pageRequest);

    Page<GetByMonthAndYear> getByMonthAndYear(PurchaseLogFilter filter, FilterSortRequest.SortRequest<PurchaseLogSortBy> sort, PageRequest pagination,MainDateFilter mainDateFilter) throws JSONException;
}

