//package com.example.auth.service;
//
//import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
//import com.example.auth.commons.decorator.ExcelUtils;
//import com.example.auth.commons.decorator.MonthConfig;
//import com.example.auth.commons.exception.NotFoundException;
//import com.example.auth.commons.helper.UserHelper;
//import com.example.auth.commons.service.AdminConfigurationService;
//import com.example.auth.commons.utils.Utils;
//import com.example.auth.decorator.*;
//import com.example.auth.decorator.pagination.FilterSortRequest;
//import com.example.auth.decorator.pagination.Pagination;
//import com.example.auth.decorator.pagination.PurchaseLogFilter;
//import com.example.auth.decorator.pagination.PurchaseLogSortBy;
//import com.example.auth.helper.PurchaseLogHistoryServiceImplTestGenerator;
//import com.example.auth.repository.CustomerRepository;
//import com.example.auth.repository.ItemRepository;
//import com.example.auth.repository.PurchaseLogHistoryRepository;
//import org.json.JSONException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class PurchaseLogHistoryResponseServiceImplTest {
//    private final static String id = "id";
//    private final static String customerId = "id";
//    private final PurchaseLogHistoryRepository purchaseLogHistoryRepository = mock(PurchaseLogHistoryRepository.class);
//    private final ModelMapper modelMapper = PurchaseLogHistoryServiceImplTestGenerator.getModelMapper();
//    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
//    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
//    private final ItemService itemService = mock(ItemService.class);
//    private final ItemRepository itemRepository = mock(ItemRepository.class);
//    private final UserHelper userHelper = mock(UserHelper.class);
//    private final AdminConfigurationService adminConfigurationService=mock(AdminConfigurationService.class);
//    private  final Utils utils=mock(Utils.class);
//
//
//    private final PurchaseLogHistoryServiceImpl purchaseLogHistoryService = spy(new PurchaseLogHistoryServiceImpl(purchaseLogHistoryRepository, modelMapper, customerRepository, nullAwareBeanUtilsBean, itemRepository, itemService, userHelper,adminConfigurationService,utils));
//
//    @Test
//    void testAddPurchaseLogHistory() {
//        //given
//
//
//        var item = PurchaseLogHistoryServiceImplTestGenerator.getItem();
//
//        Date date = purchaseLogHistoryService.currentDate();
//
//        var customer = PurchaseLogHistoryServiceImplTestGenerator.mockCustomer();
//
//        var purchaseLogHistory = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistory(date);
//
//        var purchaseLogAddRequest = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistoryAddRequest();
//
//        var purchaseLogResponse = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistoryResponse(date, null);
//
//
//        when(itemRepository.findByItemNameAndSoftDeleteIsFalse("mouse")).thenReturn(item);
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(customerId)).thenReturn(java.util.Optional.ofNullable(customer));
//
//        when(purchaseLogHistoryRepository.save(purchaseLogHistory)).thenReturn(purchaseLogHistory);
//
//        doReturn(date).when(purchaseLogHistoryService).currentDate();
//
//        //when
//        var actualData = purchaseLogHistoryService.addPurchaseLog(purchaseLogAddRequest, customerId, "mouse");
//
//        //then
//        Assertions.assertEquals(purchaseLogResponse, actualData);
//
//    }
//
//    @Test
//    void testIdNotFound() {
//        //given
//        var customer = PurchaseLogHistoryServiceImplTestGenerator.mockCustomer();
//
//        var purchaseLogAddRequest = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistoryAddRequest();
//
//        when(customerRepository.findByIdAndSoftDeleteIsFalse(customerId)).thenReturn(java.util.Optional.ofNullable(customer));
//
//        //when
//        Throwable exception = assertThrows(NotFoundException.class, () -> purchaseLogHistoryService.addPurchaseLog(purchaseLogAddRequest, null, null));
//
//        //then
//        Assertions.assertEquals("Id not found", exception.getMessage());
//
//
//    }
//
//
//    @Test
//    void testUpdatePurchaseLogHistory() throws InvocationTargetException, IllegalAccessException {
//        //given
//        Date date = purchaseLogHistoryService.currentDate();
//
//        var purchaseLogHistory = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistory(date);
//        var purchaseLogAddRequest = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistoryAddRequest();
//        var purchaseLogResponse = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistoryResponse(date, id);
//        doReturn(date).when(purchaseLogHistoryService).currentDate();
//
//        when(purchaseLogHistoryRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(purchaseLogHistory));
//
//        //when
//        purchaseLogHistoryService.updatePurchaseLogHistory(id, purchaseLogAddRequest);
//
//        //then
//        verify(purchaseLogHistoryRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
//
//    }
//
//    @Test
//    void testGetPurchaseLogHistory() {
//        //given
//        Date date = purchaseLogHistoryService.currentDate();
//        var item = PurchaseLogHistoryServiceImplTestGenerator.getItem();
//        var purchaseLogHistory = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistory(date);
//        var purchaseLogResponse = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistoryResponse(date, id);
//        doReturn(date).when(purchaseLogHistoryService).currentDate();
//        when(itemRepository.findByItemNameAndSoftDeleteIsFalse("mouse")).thenReturn(item);
//        when(purchaseLogHistoryRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(purchaseLogHistory));
//
//        //when
//        var actualData = purchaseLogHistoryService.getPurchaseLogById(id);
//
//        //then
//        Assertions.assertEquals(purchaseLogResponse, actualData);
//    }
//
//
//    @Test
//    void testGetAllPurchaseLog() {
//        //given
//        var purchaseLogHistory = PurchaseLogHistoryServiceImplTestGenerator.mockListPurchaseLogHistory();
//        var purchaseLogResponse = PurchaseLogHistoryServiceImplTestGenerator.mockListPurchaseLogHistoryResponse();
//        when(purchaseLogHistoryRepository.findAllBySoftDeleteFalse()).thenReturn(purchaseLogHistory);
//
//        //when
//        var actualData = purchaseLogHistoryService.getAllPurchaseLog();
//
//        //then
//        Assertions.assertEquals(purchaseLogResponse, actualData);
//
//    }
//
//    @Test
//    void testDeletepurchaseLog() {
//        //given
//        Date date = purchaseLogHistoryService.currentDate();
//
//        var purchaseLogHistory = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistory(date);
//        doReturn(date).when(purchaseLogHistoryService).currentDate();
//
//        when(purchaseLogHistoryRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(purchaseLogHistory));
//
//        //when
//        purchaseLogHistoryService.deletePurchaseLogById(id);
//
//        //then
//        verify(purchaseLogHistoryRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
//    }
//
//    @Test
//    void testGetAllPurchaseLogByPagination() {
//        PurchaseLogFilter filter = new PurchaseLogFilter();
//        filter.setIds(filter.getIds());
//        FilterSortRequest.SortRequest<PurchaseLogSortBy> sort = new FilterSortRequest.SortRequest<>();
//        sort.setSortBy(PurchaseLogSortBy.ITEM_NAME);
//        sort.setOrderBy(Sort.Direction.ASC);
//
//        Pagination pagination = new Pagination();
//        pagination.setPage(1);
//        pagination.setLimit(5);
//        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
//
//        var purchaseLogResponse = PurchaseLogHistoryServiceImplTestGenerator.mockListPurchaseLogHistoryResponse();
//        Page<PurchaseLogHistoryResponse> page = new PageImpl<>(purchaseLogResponse);
//        when(purchaseLogHistoryRepository.getAllPurchaseLogByPagination(filter, sort, pageRequest)).thenReturn(page);
//
//        //when
//        var actualData = purchaseLogHistoryService.getAllPurchaseLogByPagination(filter, sort, pageRequest);
//
//        //then
//        Assertions.assertEquals(page, actualData);
//    }
//
//
//    @Test
//    void testPdf() {
//        var customer = PurchaseLogHistoryServiceImplTestGenerator.mockCustomer();
//
//        var purchaseLogHistory = PurchaseLogHistoryServiceImplTestGenerator.mockListPurchaseLogHistory();
//
//
//        when(purchaseLogHistoryRepository.findByCustomerIdAndSoftDeleteFalse(customerId)).thenReturn(purchaseLogHistory);
//
//        var actualData = purchaseLogHistoryService.findById(customerId);
//
//        //then
//        Assertions.assertEquals(purchaseLogHistory, actualData);
//    }
//
//    @Test
//    void testItemPurchaseLogByIdNotFound() {
//        //given
//        var purchaseLogHistory = PurchaseLogHistoryServiceImplTestGenerator.mockPurchaseLogHistory(null);
//
//        when(purchaseLogHistoryRepository.findByIdAndSoftDeleteIsFalse(customerId)).thenReturn(java.util.Optional.ofNullable(purchaseLogHistory));
//
//        //when
//        Throwable exception = assertThrows(NotFoundException.class, () -> purchaseLogHistoryService.getItemPurchaseLogById(null));
//
//        //then
//        Assertions.assertEquals("Id not found", exception.getMessage());
//
//    }
//
//    @Test
//    void testGetPurchaseDetailsByCustomer() throws JSONException, InvocationTargetException, IllegalAccessException {
//
//        //given
//        PurchaseLogFilter filter = new PurchaseLogFilter();
//
//        filter.setIds(filter.getIds());
//
//        FilterSortRequest.SortRequest<PurchaseLogSortBy> sort = new FilterSortRequest.SortRequest<>();
//
//        sort.setSortBy(PurchaseLogSortBy.ITEM_NAME);
//
//        sort.setOrderBy(Sort.Direction.ASC);
//
//        Pagination pagination = new Pagination();
//
//        pagination.setPage(0);
//
//        pagination.setLimit(5);
//
//        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
//
//
//        var itemPurchaseAggregationResponse = PurchaseLogHistoryServiceImplTestGenerator.getItemPurchaseAggregationResponse();
//
//        Page<ItemPurchaseAggregationResponse> page = new PageImpl<>(itemPurchaseAggregationResponse);
//
//
//        HashMap<String, List<PurchaseLogExcelGenerator>> hashMap = PurchaseLogHistoryServiceImplTestGenerator.getPurchaseLogExcelGenerator();
//
//        when(purchaseLogHistoryRepository.getPurchaseDetailsByCustomer(filter, sort, pageRequest)).thenReturn(page);
//
//        //when
//        var actualData = purchaseLogHistoryService.getPurchaseDetailsByCustomer(filter, sort, pageRequest);
//
//
//        var expectedData = ExcelUtils.createWorkbookOnBookDetailsData(hashMap, "PurchaseDetails");
//
//        //then
//        Assertions.assertEquals(expectedData.getNumberOfSheets(), actualData.getNumberOfSheets());
//
//    }
//
//    @Test
//
//    void testGetByMonthAndYear() throws JSONException {
//
//        PurchaseLogFilter filter = new PurchaseLogFilter();
//
//        filter.setIds(filter.getIds());
//
//        filter.setMonth(1);
//        filter.setYear(2023);
//
//        FilterSortRequest.SortRequest<PurchaseLogSortBy> sort = new FilterSortRequest.SortRequest<>();
//
//        sort.setSortBy(PurchaseLogSortBy.ITEM_NAME);
//
//        sort.setOrderBy(Sort.Direction.ASC);
//
//        Pagination pagination = new Pagination();
//
//        pagination.setPage(0);
//
//        pagination.setLimit(5);
//
//        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
//
////        var monthConfig=PurchaseLogHistoryServiceImplTestGenerator.getMonthConfig();
//        MonthConfig monthConfig=new MonthConfig();
//
//        var mainDateFilter=purchaseLogHistoryService.getMainDateFilter(monthConfig,filter);
//
//
//        var mainDate=PurchaseLogHistoryServiceImplTestGenerator.getMainDateFilter();
//
////        var purchaseLogHistoryFilter =PurchaseLogHistoryServiceImplTestGenerator.getPurchaseLogHistoryFilter();
//
//        List<PurchaseLogHistoryFilter> dateFilters=PurchaseLogHistoryServiceImplTestGenerator.getPurchaseHistoryFilter();
//
//
//        var getByMonthAndYear=PurchaseLogHistoryServiceImplTestGenerator.getByMonthAndYear();
//
////        when(purchaseLogHistoryService.getMainDateFilter(monthConfig,filter)).thenReturn();
//
//
//
//        Page<GetByMonthAndYear> page=new PageImpl<>(getByMonthAndYear);
//
//        when(purchaseLogHistoryRepository.getByMonthAndYear(filter,sort,pageRequest,mainDate)).thenReturn(page);
//
//
//        var actualData=purchaseLogHistoryService.getByMonthAndYear(filter,sort,pageRequest,mainDate);
//
//
//        Assertions.assertEquals(page,actualData);
//
//
//    }
////
////    The error message is indicating that the actual data returned by the method purchaseLogHistoryService.
////    getByMonthAndYear(filter, sort, pageRequest, mainDate) is null, but you were expecting a Page object.
////    This means that the method is not returning the expected result.
////
////    To solve the issue, you can try the following steps:
////
////    Check if the method purchaseLogHistoryRepository.getByMonthAndYear(filter, sort, pageRequest, mainDate)
////    is returning the correct result.
////
////    Verify that the correct arguments are being passed to the method purchaseLogHistoryRepository.
////    getByMonthAndYear(filter, sort, pageRequest, mainDate).
////
////    Check if the method purchaseLogHistoryService.getMainDateFilter(monthConfig, filter) is returning the
////    correct result.
////
////    If you have any logging statements in the code, check the logs to see if there is any information that
////    can help you understand what's causing the issue.
////
////    If none of these steps solve the issue, you may want to add additional logging statements or print statements
////    to the code to help you identify the root cause of the issue.
//
//
//
//
//
//
//}