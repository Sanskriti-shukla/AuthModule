package com.example.auth.stockPile.service;

import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.stockPile.decorator.*;

import com.example.auth.stockPile.model.Stock;
import com.example.auth.stockPile.model.Subscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface StockService {

    StockResponse addStock(StockAddRequest stockAddRequest);


    StockResponse getStockById(String id);

    void updateStock(String id, StockAddRequest stockAddRequest) throws NoSuchFieldException, IllegalAccessException;

    void deleteStockById(String id);

    List<StockResponse> getAllStock();


    Page<StockResponse> getAllStockByPagination(StockFilter filter, FilterSortRequest.SortRequest<StockSortBy> sort, PageRequest pagination);

    String subscribeUnsubscribeStock(String symbol, String userId, Subscribe subscribe);

    Map<String, List<Stock>> allSubscribers();


    StockResponse getStockBySymbol(String symbol);

    List<StockSubscribed> subscribedStocksByUserId(String userId);
}
