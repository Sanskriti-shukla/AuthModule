package com.example.auth.stockPileTest.service;

import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.stockPile.model.Stock;
import com.example.auth.stockPile.model.Subscribe;
import com.example.auth.stockPile.model.Subscriber;
import com.example.auth.stockPile.model.UserData;
import com.example.auth.stockPile.repository.StockRepository;
import com.example.auth.stockPile.repository.SubscriberRepository;
import com.example.auth.stockPile.repository.UserDataRepository;
import com.example.auth.stockPile.service.StockService;
import com.example.auth.stockPile.service.StockServiceImpl;
import com.example.auth.stockPile.service.UserDataServiceImpl;
import com.example.auth.stockPileTest.helper.StockServiceImplTestGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
 class StockServiceImplTest {
    private final static String stockId = "id";
    private final static String userId = "id";
    private final static Subscribe subscribe = Subscribe.SUBSCRIBE;
    private final StockRepository stockRepository = mock(StockRepository.class);
    private final UserHelper userHelper = mock(UserHelper.class);
    private final ModelMapper modelMapper = StockServiceImplTestGenerator.getModelMapper();
    private final UserDataServiceImpl userDataService = mock(UserDataServiceImpl.class);
    private final UserDataRepository userDataRepository = mock(UserDataRepository.class);
    private final SubscriberRepository subscriberRepository = mock(SubscriberRepository.class);

    public StockService stockService = new StockServiceImpl(stockRepository,userHelper,modelMapper,userDataService,userDataRepository,subscriberRepository);

//    @Test
//    void  testAddStock(){
//        var stock = StockServiceImplTestGenerator.mockStock();
//
//        var stockResponse = StockServiceImplTestGenerator.mockStockResponse();
//
//        var stockAddRequest = StockServiceImplTestGenerator.mockStockAddRequest();
//
//        when(stockRepository.existsBySymbolAndSoftDeleteIsFalse(stockAddRequest.getSymbol())).thenReturn(true);
//
//        when(stockRepository.findBySymbolAndSoftDeleteIsFalse(stockAddRequest.getSymbol())).thenReturn(java.util.Optional.ofNullable(stock));
//
//        when(stockRepository.save(stock)).thenReturn(stock);
//
//        var actualData = stockService.addStock(stockAddRequest);
//
//        Assertions.assertEquals(stockResponse,actualData);
//    }


    @Test
    void testUpdateStock() throws NoSuchFieldException, IllegalAccessException {
        var stock = StockServiceImplTestGenerator.mockStock();

        var stockAddRequest = StockServiceImplTestGenerator.mockStockAddRequest();

        when(stockRepository.findByIdAndSoftDeleteIsFalse(stockId)).thenReturn(java.util.Optional.ofNullable(stock));

        stockService.updateStock(stockId,stockAddRequest);

        verify(stockRepository, times(2)).findByIdAndSoftDeleteIsFalse(stockId);
    }

    @Test

    void testStockById(){
        var stock = StockServiceImplTestGenerator.mockStock();

        var stockResponse = StockServiceImplTestGenerator.mockStockResponse();

        when(stockRepository.findByIdAndSoftDeleteIsFalse(stockId)).thenReturn(java.util.Optional.ofNullable(stock));

        var actualData = stockService.getStockById(stockId);

        Assertions.assertEquals(stockResponse,actualData);
    }


    @Test
    void testGetAllStock(){

        var stockResponse  = StockServiceImplTestGenerator.mockAllStockResponse();

        var stocks = StockServiceImplTestGenerator.mockAllStock();

        when(stockRepository.findAllBySoftDeleteFalse()).thenReturn(stocks);

        var actualData = stockService.getAllStock();

        Assertions.assertEquals(stockResponse,actualData);
    }

    @Test

    void  testDeleteStockById(){

        var stock = StockServiceImplTestGenerator.mockStock();

        when(stockRepository.findByIdAndSoftDeleteIsFalse(stockId)).thenReturn(java.util.Optional.ofNullable(stock));


        when(stockRepository.save(stock)).thenReturn(stock);

        stockService.deleteStockById(stockId);

        verify(stockRepository,times(1)).findByIdAndSoftDeleteIsFalse(stockId);
    }


     @Test
    void testSubscribeUnsubscribeStock(){
         Date date = new Date();

         var stock = StockServiceImplTestGenerator.mockStock();

         var userData = StockServiceImplTestGenerator.mockUserData();

         var subscriber = StockServiceImplTestGenerator.mockSubscriber(date,subscribe);

         when(stockRepository.findBySymbolAndSoftDeleteIsFalse(stock.getSymbol())).thenReturn(java.util.Optional.of(stock));

         when(userDataService.userById(userId)).thenReturn(userData);

         when(subscriberRepository.save(subscriber)).thenReturn(subscriber);

         when(stockRepository.save(stock)).thenReturn(stock);

         when(userDataRepository.save(userData)).thenReturn(userData);

         var actualData = stockService.subscribeUnsubscribeStock(stock.getSymbol(), userId,subscribe);

         Assertions.assertEquals(subscriber.getId(), actualData);
     }

     @Test
     void testAllSubscribers(){

        var stocks = StockServiceImplTestGenerator.mockAllSubscriber();

         var stocks1 = StockServiceImplTestGenerator.mockAllStock();


         when(stockRepository.findAllBySoftDeleteFalse()).thenReturn(stocks1);

        var actualData = stockService.allSubscribers();

        Assertions.assertEquals(stocks,actualData);

     }

     @Test
    void testGetStockBySymbol(){

         var stock = StockServiceImplTestGenerator.mockStock();


         var stockResponse = StockServiceImplTestGenerator.mockStockResponse();

         when(stockRepository.findBySymbolAndSoftDeleteIsFalse(stock.getSymbol())).thenReturn(java.util.Optional.of(stock));

         var actualData = stockService.getStockBySymbol(stock.getSymbol());

         Assertions.assertEquals(stockResponse,actualData);
     }

     @Test
    void testSubscribedStocksByUserId(){
        Date date = new Date();

        var stockSubscriberId = StockServiceImplTestGenerator.mockStockSubscribed();

        var subscriber = StockServiceImplTestGenerator.mockAllSubscribers(date,subscribe);

         var stocks = StockServiceImplTestGenerator.mockAllStock();

         when(subscriberRepository.findAllByUserId(userId)).thenReturn(subscriber);

         when(stockRepository.findAllByIdAndSoftDeleteFalse(stockId)).thenReturn(stocks);

         var actualData = stockService.subscribedStocksByUserId(userId);

         Assertions.assertEquals(stockSubscriberId,actualData);

     }

}
