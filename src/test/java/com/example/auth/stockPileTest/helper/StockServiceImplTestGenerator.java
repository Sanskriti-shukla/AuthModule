package com.example.auth.stockPileTest.helper;

import com.example.auth.stockPile.decorator.StockAddRequest;
import com.example.auth.stockPile.decorator.StockResponse;
import com.example.auth.stockPile.decorator.StockSubscribed;
import com.example.auth.stockPile.model.Stock;
import com.example.auth.stockPile.model.Subscribe;
import com.example.auth.stockPile.model.Subscriber;
import com.example.auth.stockPile.model.UserData;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.*;

public class StockServiceImplTestGenerator {
    public static final List<String> subscribers= new ArrayList<>();
    public static final String stockId="id";
    public static final String userId="id";
    public static final String subscriberId="id";
//    public static final Subscribe subscribe=true;
    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public static StockAddRequest mockStockAddRequest(){
        return StockAddRequest.builder()
                .symbol("AAPL")
                .name("sans")
//                .subscribers(subscribers)
                .description("hello")
                .build();
    }
    public static StockResponse mockStockResponse(){
        return StockResponse.builder()
                .id(stockId)
                .symbol("AAPL")
                .name("sans")
                .description("hello")
                .subscribers(subscribers)
                .build();
    }

    public static Stock mockStock(){
        return Stock.builder()
                .id(stockId)
                .symbol("AAPL")
                .subscribers(subscribers)
                .name("sans")
                .description("hello")
                .build();
    }


    public static List<Stock> mockAllStock(){
        return List.of( Stock.builder()
                .id(stockId)
                .symbol("AAPL")
                .subscribers(subscribers)
                .name("sans")
                .description("hello")
                .build());
    }
    public static List<StockResponse> mockAllStockResponse(){
        return List.of(StockResponse.builder()
                .id(stockId)
                .symbol("AAPL")
                .name("sans")
                .description("hello")
                .subscribers(subscribers)
                .build());
    }
    public static UserData mockUserData(){
        return UserData.builder()
                .subscribe(true)
                .id(userId)
                .email("sans@gmail.com")
                .userName("sans")
                .name("sans")
                .contact("6385254154")
                .build();
    }


    public static Subscriber mockSubscriber(Date date,Subscribe subscribe) {
        return Subscriber.builder()
                .id(subscriberId)
                .userId(userId)
                .stockid(stockId)
                .subscribe(subscribe)
                .createdOn(date)
                .build();

    }
    public static List<Subscriber> mockAllSubscribers(Date date,Subscribe subscribe){
        return List.of(Subscriber.builder()
                .id(subscriberId)
                .userId(userId)
                .stockid(stockId)
                .subscribe(subscribe)
                .createdOn(date)
                .build());
    }


public static Map<String, List<Stock>> mockAllSubscriber() {
    Map<String, List<Stock>> map = new HashMap<>();
    List<Stock> stockList = new ArrayList<>();
    stockList.add(Stock.builder()
            .id(stockId)
            .symbol("AAPL")
            .subscribers(subscribers)
            .name("sans")
            .description("hello")
            .build());
    map.put("AAPL", stockList);
    return map;
}

public static List<StockSubscribed> mockStockSubscribed(){
        return List.of(StockSubscribed.builder()
                .stockId(stockId)
                .symbol("AAPL")
                .name("sans")
                .build());
}

}
