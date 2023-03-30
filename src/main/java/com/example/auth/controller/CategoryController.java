package com.example.auth.controller;

import com.example.auth.commons.Access;
import com.example.auth.commons.constant.ResponseConstant;
import com.example.auth.commons.decorator.GeneralHelper;
import com.example.auth.commons.enums.Role;
import com.example.auth.commons.decorator.DataResponse;
import com.example.auth.commons.decorator.ListResponse;
import com.example.auth.commons.decorator.Response;
import com.example.auth.decorator.category.CategoryAddRequest;
import com.example.auth.decorator.category.CategoryResponse;
import com.example.auth.decorator.pagination.CategoryFilter;
import com.example.auth.decorator.pagination.CategorySortBy;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.PageResponse;
import com.example.auth.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categroies")
public class CategoryController {
    private final CategoryService categoryService;
    private final GeneralHelper generalHelper;

    public CategoryController(CategoryService categoryService, GeneralHelper generalHelper) {
        this.categoryService = categoryService;
        this.generalHelper = generalHelper;
    }


    @PostMapping(name = "addCategory" , value = "/add/category")
    @Access(levels = Role.ADMIN)
    public DataResponse<CategoryResponse> addCategory(@RequestBody CategoryAddRequest categoryAddRequest) {
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(categoryService.addCategory(categoryAddRequest));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;
    }


    @PutMapping(name = "updateCategory"  , value = "/update/category")
    @Access (levels = Role.ADMIN)
    public DataResponse<CategoryResponse> updateCategory(@RequestParam String id, @RequestBody CategoryAddRequest categoryAddRequest) {
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(categoryService.updateCategory(id, categoryAddRequest));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UPDATED_SUCCESSFULLY));
        return dataResponse;
    }



    @GetMapping(name = "getCategoryById" , value = "/category/{id}")
    @Access (levels = Role.ADMIN)
    public DataResponse<CategoryResponse> getCategoryById(@PathVariable String id) {
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(categoryService.getCategoryById(id));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }

    @GetMapping(name = "getAllCategory" , value = "/getAllCategroy")
    @Access (levels = Role.ADMIN)
    public ListResponse<CategoryResponse> getAllCategory() {
        ListResponse<CategoryResponse> listResponse = new ListResponse<>();
        listResponse.setData(categoryService.getAllCategory());
        listResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));

        return listResponse;
    }


    @DeleteMapping(name = "deleteCategory"  ,value = "/delete/{id}")
    @Access (levels = Role.ADMIN)
    public DataResponse<Object> deleteCategory(@PathVariable String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        categoryService.deleteCategory(id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.DELETED_SUCCESSFULLY));
        return dataResponse;
    }


    @PostMapping(name = "getAllCategoryByPagination" , value = "/get/all/pagination")
    @Access (levels = Role.ADMIN)
    public PageResponse<CategoryResponse> getAllCategoryByPagination(@RequestBody FilterSortRequest<CategoryFilter, CategorySortBy> filterSortRequest) {
        PageResponse<CategoryResponse> pageResponse = new PageResponse<>();
        Page<CategoryResponse> categoryrResponses = categoryService.getAllCategoryByPagination(filterSortRequest.getFilter(), filterSortRequest.getSort(), generalHelper.getPagination(filterSortRequest.getPagination().getPage(), filterSortRequest.getPagination().getLimit()));
        pageResponse.setData(categoryrResponses);
        pageResponse.setStatus(Response.getOkResponse());
        return pageResponse;
    }

}
