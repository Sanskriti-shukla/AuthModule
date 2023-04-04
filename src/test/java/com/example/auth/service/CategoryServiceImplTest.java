package com.example.auth.service;

import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.decorator.category.CategoryResponse;
import com.example.auth.decorator.pagination.*;
import com.example.auth.helper.CategoryServiceImplGenerator;
import com.example.auth.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceImplTest {
    private static final String id = "id";
    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final ModelMapper modelMapper = CategoryServiceImplGenerator.getModelMapper();
    private final ItemService itemService = Mockito.mock(ItemService.class);
    private final AdminConfigurationService adminConfigurationService=Mockito.mock(AdminConfigurationService.class);
    public CategoryService categoryService = new CategoryServiceImpl(categoryRepository, modelMapper, itemService, adminConfigurationService);


    @Test
    void testUpdateCategory() throws InvocationTargetException, IllegalAccessException {
        //given
        AdminConfiguration adminConfiguration = adminConfigurationService.getConfiguration();
        var category = CategoryServiceImplGenerator.MockCategory();
        var addCategory = CategoryServiceImplGenerator.MockAddCategory();
        var categoryresponse = CategoryServiceImplGenerator.MockCategoryResponse();
        when(categoryRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        //when
        var actualData = categoryService.updateCategory(id, addCategory);

        //then
        Assertions.assertEquals(categoryresponse, actualData);
    }

    @Test
    void testAddCategory() throws InvocationTargetException, IllegalAccessException {
        //given

        var adminConfig=CategoryServiceImplGenerator.getMockAdminConfig();
        var category = CategoryServiceImplGenerator.MockCategory();
        var addCategory = CategoryServiceImplGenerator.MockAddCategory();
        var categoryresponse = CategoryServiceImplGenerator.MockCategoryResponse();
        when(categoryRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(category));
        when(adminConfigurationService.getConfiguration()).thenReturn(adminConfig);
        when(categoryRepository.save(category)).thenReturn(category);

        //when
        var actualData = categoryService.addCategory(addCategory);

        //then
        Assertions.assertEquals(categoryresponse, actualData);
    }


    @Test
    void getCategoryById() {
        //given
        var category = CategoryServiceImplGenerator.MockCategory();
        var Categoryresponse = CategoryServiceImplGenerator.MockCategoryResponse();
        when(categoryRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(category));

        //when
        var actualData = categoryService.getCategoryById(id);

        //then
        Assertions.assertEquals(Categoryresponse, actualData);
    }

    @Test
    void testGetAllCategory() {
        //given
        var category = CategoryServiceImplGenerator.MockCategories();
        var Categoryresponse = CategoryServiceImplGenerator.getMockResponse();
        when(categoryRepository.findAllBySoftDeleteFalse()).thenReturn(category);

        //when
        var actualData = categoryService.getAllCategory();

        //then
        Assertions.assertEquals(Categoryresponse, actualData);
    }


    @Test
    void testDeleteCategory() {
        //given
        var category = CategoryServiceImplGenerator.MockCategory();
        Mockito.when(categoryRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(category));

        //when
        categoryService.deleteCategory(id);
        //then
        verify(categoryRepository, times(1)).findByIdAndSoftDeleteIsFalse((id));

    }

    @Test
  void   testgetCategoryByPagination(){
        //given
        CategoryFilter categoryFilter= new CategoryFilter();
        categoryFilter.setId(categoryFilter.getId());
        FilterSortRequest.SortRequest<CategorySortBy> sort=new FilterSortRequest.SortRequest<>();
         sort.setSortBy(CategorySortBy.CATEGORY_NAME);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination= new Pagination();
        pagination.setLimit(2);
        pagination.setPage(0);
        PageRequest pageRequest=PageRequest.of(pagination.getPage(), pagination.getLimit());
        var categoryResponse=CategoryServiceImplGenerator.getMockResponse();
        Page<CategoryResponse> category=new PageImpl<>(categoryResponse);
        when( categoryRepository.getAllCategoryByPagination(categoryFilter,sort,pageRequest)).thenReturn(category);

        //when
        var actualData=categoryService.getAllCategoryByPagination(categoryFilter,sort,pageRequest);


        //then
        Assertions.assertEquals(category,actualData);
    }

}
