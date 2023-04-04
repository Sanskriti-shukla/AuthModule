package com.example.auth.controller;


import com.example.auth.commons.Access;
import com.example.auth.commons.constant.ResponseConstant;
import com.example.auth.commons.decorator.DataResponse;
import com.example.auth.commons.decorator.GeneralHelper;
import com.example.auth.commons.decorator.ListResponse;
import com.example.auth.commons.decorator.Response;
import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.PageResponse;
import com.example.auth.stockPile.decorator.*;
import com.example.auth.stockPile.model.Stock;
import com.example.auth.stockPile.model.Subscribe;
import com.example.auth.stockPile.service.StockService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("stock_info")
public class StockController {

    private final StockService stockService;
    private final GeneralHelper generalHelper;

    public StockController(StockService stockService, GeneralHelper generalHelper) {
        this.stockService = stockService;
        this.generalHelper = generalHelper;
    }

//    @RequestMapping(name = "addStock", value = "/add", method = RequestMethod.POST)
    @PostMapping(name = "addStock" , value = "/add/stock")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<StockResponse> addStock(@RequestBody StockAddRequest stockAddRequest) {
        DataResponse<StockResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(stockService.addStock(stockAddRequest));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;
    }

//    @RequestMapping(name = "updateStock", value = "/update/stock", method = RequestMethod.POST)
    @PutMapping(name = "updateStock" , value = "/update/stock")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> updateStock(@PathVariable String id, @RequestBody StockAddRequest stockAddRequest) throws NoSuchFieldException, IllegalAccessException {
        DataResponse<Object> dataResponse = new DataResponse<>();
        stockService.updateStock(id, stockAddRequest);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UPDATED_SUCCESSFULLY));
        return dataResponse;
    }

//    @RequestMapping(name = "getStockById", value = "/{id}", method = RequestMethod.GET)
    @GetMapping(name = "getStockById" , value = "/stock/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<StockResponse> getStockById(@PathVariable String id) {
        DataResponse<StockResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(stockService.getStockById(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }


//    @RequestMapping(name = "getStockBySymbol", value = "/symbol", method = RequestMethod.GET)
    @GetMapping(name = "getStockBySymbol" , value = "/symbol")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<StockResponse> getStockBySymbol(@RequestParam String symbol) {
        DataResponse<StockResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(stockService.getStockBySymbol(symbol));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

//    @RequestMapping(name = "getAllStock", value = "/get/all/stock", method = RequestMethod.GET)
    @GetMapping(name = "getAllStock" , value = "/get/all/stock")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<StockResponse> getAllStock() {
        ListResponse<StockResponse> listResponse = new ListResponse<>();
        listResponse.setData(stockService.getAllStock());
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }


//    @RequestMapping(name = "deleteStockById", value = "/{id}", method = RequestMethod.POST)
    @DeleteMapping(name = "deleteStockById", value = "/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> deleteStockById(@PathVariable String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        stockService.deleteStockById(id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.DELETED_SUCCESSFULLY));
        return dataResponse;
    }

//    @RequestMapping(name = "getAllStockByPagination", value = "get/all/pagination", method = RequestMethod.POST)
    @PostMapping(name = "getAllStockByPagination" , value = "/get/all/pagination")
    @Access(levels = Role.ANONYMOUS)
    public PageResponse<StockResponse> getAllStockByPagination(@RequestBody FilterSortRequest<StockFilter, StockSortBy> filterSortRequest) {
        PageResponse<StockResponse> pageResponse = new PageResponse<>();
        Page<StockResponse> page = stockService.getAllStockByPagination(filterSortRequest.getFilter(), filterSortRequest.getSort(),
                generalHelper.getPagination(filterSortRequest.getPagination().getPage(), filterSortRequest.getPagination().getLimit()));
        pageResponse.setData(page);
        pageResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return pageResponse;

    }

//    @RequestMapping(name = "subscribeUnsubscribeStock", value = "/subscription/unsubscribe", method = RequestMethod.GET)
    @GetMapping(name = "subscribeUnsubscribeStock" , value = "/subscription/unsubscribe")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<String> getStockSubscription(@RequestParam String symbol, @RequestParam String userId, @RequestParam Subscribe subscribe) {
        DataResponse<String> dataResponse = new DataResponse<>();
        if (subscribe == Subscribe.SUBSCRIBE) {
            dataResponse.setData(stockService.subscribeUnsubscribeStock(symbol, userId, subscribe));
            dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SUBSCRIBED_SUCESSFULLY));
        } else {
            dataResponse.setData(stockService.subscribeUnsubscribeStock(symbol, userId, subscribe));
            dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UNSUBSCRIBED_SUCESSFULLY));
        }
        return dataResponse;

    }

//    @RequestMapping(name = "allSubscribers", value = "/all/subscribers", method = RequestMethod.POST)
    @PostMapping(name = "allSubscribers" ,value = "/all/subscribers")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Map<String, List<Stock>>> allSubscribers() {
        DataResponse<Map<String, List<Stock>>> dataResponse = new DataResponse<>();
        dataResponse.setData(stockService.allSubscribers());
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }

//    @RequestMapping(name = "subscribedStocksByUserId", value = "subscribedStocks/byUserId", method = RequestMethod.POST)
    @PostMapping(name = "subscribedStocksByUserId" ,  value = "/subscribedStocks/byUserId")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<StockSubscribed> subscribedStocksByUserId(String userId) {
        ListResponse<StockSubscribed> listResponse = new ListResponse<>();
        listResponse.setData(stockService.subscribedStocksByUserId(userId));
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }


}
