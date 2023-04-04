package com.example.auth.service;

import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.decorator.ItemAddRequest;
import com.example.auth.decorator.ItemAggregationResponse;
import com.example.auth.decorator.ItemResponse;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.ItemFilter;
import com.example.auth.decorator.pagination.ItemSortBy;
import com.example.auth.model.Category;
import com.example.auth.model.Item;
import com.example.auth.repository.CategoryRepository;
import com.example.auth.repository.ItemRepository;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    private final UserHelper userHelper;


    public ItemServiceImpl(CategoryRepository categoryRepository, ItemRepository itemRepository, ModelMapper modelMapper, NullAwareBeanUtilsBean nullAwareBeanUtilsBean, UserHelper userHelper) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.userHelper = userHelper;
    }


    @Override
    public ItemResponse addItem(String categoryId, ItemAddRequest itemAddRequest) {

        checkValidation(itemAddRequest);

        Item item = modelMapper.map(itemAddRequest, Item.class);

        Category category = getCategoryModel(categoryId);

        item.setCategoryId(category.getId());

        setPrice(itemAddRequest, item);

        item.setDate(currentDate());

        ItemResponse itemResponse = modelMapper.map(item, ItemResponse.class);

        itemRepository.save(item);

        return itemResponse;
    }

    @VisibleForTesting
    Date currentDate() {
        return new Date();
    }

    @Override
    public ItemResponse updateItem(String id, ItemAddRequest itemAddRequest) {

        Item item = getById(id);

        ItemResponse itemResponse = modelMapper.map(item, ItemResponse.class);

        updateItemData(id, itemAddRequest);

        try {
            userHelper.difference(item, itemAddRequest);
        }  catch (NoSuchFieldException| IllegalAccessException  e) {
            log.error("error occured : {}",e.getMessage(), e);
        }

        return itemResponse;
    }


    @Override
    public ItemResponse getItemById(String id) {

        Item item = getById(id);
        ItemResponse itemResponse = modelMapper.map(item, ItemResponse.class);
        return itemResponse;
    }

    @Override
    public List<ItemResponse> getAllItems() {

        List<Item> items = itemRepository.findBySoftDeleteFalse();
        List<ItemResponse> itemResponses = new ArrayList<>();
        items.forEach(item -> {
            ItemResponse itemResponse = modelMapper.map(item, ItemResponse.class);
            itemResponses.add(itemResponse);

        });
        return itemResponses;
    }

    @Override
    public void deleteItemById(String id) {

        Item item = getById(id);
        item.setSoftDelete(true);
        itemRepository.save(item);

    }


    public void setPrice(ItemAddRequest itemAddRequest, Item item) {

        item.setPrice(Double.parseDouble(new DecimalFormat("##.##").format(itemAddRequest.getPrice())));
        item.setDiscountInRupee((item.getPrice() * item.getQuantity() * item.getDiscountInPercent()) / 100);
        item.setTotalPrice(item.getPrice() * item.getQuantity() - item.getDiscountInRupee());
    }


    @Override
    public List<ItemAggregationResponse> getItemByAggregation() {
        return itemRepository.getItemByAggregation();
    }
    @Override
    public Page<ItemResponse> getAllItemsByPagination(ItemFilter filter, FilterSortRequest.SortRequest<ItemSortBy> sort, PageRequest pageRequest) {
        return itemRepository.getAllItemsByPagination(filter, sort, pageRequest);
    }


    public Category getCategoryModel(String categoryId) {
        return categoryRepository.findByIdAndSoftDeleteIsFalse(categoryId).orElseThrow(() -> new NotFoundException(MessageConstant.ID_NOT_FOUND));
    }

    private Item getById(String id) {
        return itemRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.ID_NOT_FOUND));

    }

    @Override
    public void removeItems(String id) {

        List<Item> items = itemRepository.findByCategoryIdAndSoftDeleteFalse(id);
        if (!CollectionUtils.isEmpty(items)) {
            items.forEach(item -> {
                item.setSoftDelete(true);
            });
            itemRepository.saveAll(items);
        }
    }

    public void checkValidation(ItemAddRequest itemAddRequest) {
        if (itemAddRequest.getPrice() < 0) {
            throw new NotFoundException(MessageConstant.PRICE_MUST_NOT_BE_NULL);
        }

        if (itemAddRequest.getQuantity() < 0) {
            throw new NotFoundException(MessageConstant.MUST_NOT_BE_NULL);
        }

        if (itemAddRequest.getItemName().isEmpty()) {
            throw new NotFoundException(MessageConstant.NAME_MUST_NOT_BE_NULL);

        }
        if (itemRepository.existsByItemNameAndSoftDeleteIsFalse(itemAddRequest.getItemName())) {
            throw new NotFoundException(MessageConstant.ALREADY_EXIST);
        }
        if (!itemAddRequest.getItemName().matches("^[A-Za-z]+(([,.] |[ '-])[A-Za-z]+)*([.,'-]?)$")) {
            throw new NotFoundException(MessageConstant.INVALID);
        }

    }


    public void updateItemData(String id, ItemAddRequest itemAddRequest)  {

        Item item = getById(id);
        if (item != null) {
            if (itemAddRequest.getQuantity() > 0) {
                item.setQuantity(itemAddRequest.getQuantity());
                item.setDiscountInRupee((item.getPrice() * item.getQuantity() * item.getDiscountInPercent()) / 100);
                item.setTotalPrice(item.getPrice() * item.getQuantity() - item.getDiscountInRupee());
            }
            if (itemAddRequest.getItemName() != null) {
                item.setItemName(itemAddRequest.getItemName());
            }
            if (itemAddRequest.getPrice() > 0) {
                item.setPrice(Double.parseDouble(new DecimalFormat("##.##").format(itemAddRequest.getPrice())));
                setPrice(itemAddRequest, item);
            }
            if (itemAddRequest.getDiscountInPercent() > 0) {
                item.setDiscountInPercent(itemAddRequest.getDiscountInPercent());
                item.setDiscountInRupee((item.getPrice() * item.getQuantity() * item.getDiscountInPercent()) / 100);
                item.setTotalPrice(item.getPrice() * item.getQuantity() - item.getDiscountInRupee());
            }
            itemRepository.save(item);
        }
    }

}


