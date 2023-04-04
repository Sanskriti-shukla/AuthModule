package com.example.auth.service;

import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.exception.InvalidRequestException;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.decorator.category.CategoryAddRequest;
import com.example.auth.decorator.category.CategoryResponse;
import com.example.auth.decorator.pagination.*;
import com.example.auth.model.Category;
import com.example.auth.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ItemService itemService;
    private final AdminConfigurationService adminConfigurationService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ItemService itemService, AdminConfigurationService adminConfigurationService) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.itemService = itemService;
        this.adminConfigurationService = adminConfigurationService;
    }

    public CategoryResponse addCategory(CategoryAddRequest categoryAddRequest) {

        Category category = modelMapper.map(categoryAddRequest, Category.class);
        category.setDate(new Date());
        CategoryResponse categoryResponse = modelMapper.map(categoryAddRequest, CategoryResponse.class);
        checkValidation(categoryAddRequest);
        categoryRepository.save(category);
        return categoryResponse;
    }

    @Override
    public CategoryResponse updateCategory(String id, CategoryAddRequest categoryAddRequest) {

        Category category = getById(id);
        category.setDate(new Date());
        category.setCategoryName(categoryAddRequest.getCategoryName());
        CategoryResponse categoryResponse = modelMapper.map(categoryAddRequest, CategoryResponse.class);
        categoryRepository.save(category);
        return categoryResponse;
    }

    @Override
    public CategoryResponse getCategoryById(String id) {
        Category category = getById(id);
    return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategory() {

        List<Category> category = categoryRepository.findAllBySoftDeleteFalse();
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        category.forEach(category1 -> {
            CategoryResponse categoryResponse = modelMapper.map(category1, CategoryResponse.class);
            categoryResponses.add(categoryResponse);
        });
        return categoryResponses;
    }

    @Override
    public void deleteCategory(String id) {

        Category category = getById(id);
        itemService.removeItems(id);
        category.setSoftDelete(true);
        categoryRepository.save(category);
    }


    public void checkValidation(CategoryAddRequest categoryAddRequest)  {
        
        AdminConfiguration adminConfiguration = adminConfigurationService.getConfiguration();

        if (categoryRepository.existsBycategoryNameAndSoftDeleteIsFalse(categoryAddRequest.getCategoryName())) {
            throw new InvalidRequestException(MessageConstant.ALREADY_EXIST);
        }
        if (!categoryAddRequest.getCategoryName().matches(adminConfiguration.getNameRegex())) {
            throw new InvalidRequestException(MessageConstant.NAME_MUST_NOT_BE_NULL);
        }

    }

    @Override
    public Page<CategoryResponse> getAllCategoryByPagination(CategoryFilter filter, FilterSortRequest.SortRequest<CategorySortBy> sort, PageRequest pageRequest) {
        return categoryRepository.getAllCategoryByPagination(filter, sort, pageRequest);
    }


    public Category getById(String id) throws NotFoundException {
        return categoryRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_ID_NOT_FOUND));

    }

}


