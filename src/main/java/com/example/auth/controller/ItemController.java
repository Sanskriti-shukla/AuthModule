package com.example.auth.controller;

import com.example.auth.commons.Access;
import com.example.auth.commons.constant.ResponseConstant;
import com.example.auth.commons.decorator.DataResponse;
import com.example.auth.commons.decorator.GeneralHelper;
import com.example.auth.commons.decorator.ListResponse;
import com.example.auth.commons.decorator.Response;
import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.*;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.ItemFilter;
import com.example.auth.decorator.pagination.ItemSortBy;
import com.example.auth.decorator.pagination.PageResponse;
import com.example.auth.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("items")
public class ItemController {
    private final ItemService itemService;
    private final GeneralHelper generalHelper;

    public ItemController(ItemService itemService, GeneralHelper generalHelper) {
        this.itemService = itemService;
        this.generalHelper = generalHelper;
    }

    @PostMapping(name = "addItem" , value = "/add")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<ItemResponse> addItem(@RequestParam(required = false) String categoryId, @RequestBody ItemAddRequest itemAddRequest) {
        DataResponse<ItemResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(itemService.addItem(categoryId, itemAddRequest));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;
    }

    @PutMapping(name = "updateItem" , value = "/update")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<ItemResponse> updateItem(@RequestParam String id, @RequestBody ItemAddRequest itemAddRequest) {
        DataResponse<ItemResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(itemService.updateItem(id, itemAddRequest));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UPDATED_SUCCESSFULLY));
        return dataResponse;
    }


    @GetMapping(name = "getItemById" , value = "item/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<ItemResponse> getItemById(@RequestParam String id) {
        DataResponse<ItemResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(itemService.getItemById(id));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }


    @GetMapping(name = "getAllItems" , value = "/getAll")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<ItemResponse> geAllItems() {
        ListResponse<ItemResponse> listResponse = new ListResponse<>();
        listResponse.setData(itemService.getAllItems());
        listResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return listResponse;
    }

    @DeleteMapping(name = "deleteItemById" , value = "/delete/{d}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> deleteItemById(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        itemService.deleteItemById(id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.DELETED_SUCCESSFULLY));
        return dataResponse;
    }


    @PostMapping(name = "getAllItemsByPagination" , value = "/get/all/pagination")
    @Access(levels = Role.ANONYMOUS)
    public PageResponse<ItemResponse> getAllItemsByPagination(@RequestBody FilterSortRequest<ItemFilter, ItemSortBy> filterSortRequest) {
        PageResponse<ItemResponse> pageResponse = new PageResponse<>();
        Page<ItemResponse> itemResponses = itemService.getAllItemsByPagination(filterSortRequest.getFilter(), filterSortRequest.getSort(), generalHelper.getPagination(filterSortRequest.getPagination().getPage(), filterSortRequest.getPagination().getLimit()));
        pageResponse.setData(itemResponses);
        pageResponse.setStatus(Response.getOkResponse());
        return pageResponse;
    }


    @PostMapping(name = "getItemByAggregation"  ,value = "/item/aggregation")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<ItemAggregationResponse> getItemByAggergation() {
        ListResponse<ItemAggregationResponse> listResponse = new ListResponse<>();
        listResponse.setData(itemService.getItemByAggregation());
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }

}
