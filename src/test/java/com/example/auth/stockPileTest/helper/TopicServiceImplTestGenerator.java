package com.example.auth.stockPileTest.helper;

import com.example.auth.stockPile.decorator.Title;
import com.example.auth.stockPile.decorator.TopicAddRequest;
import com.example.auth.stockPile.decorator.TopicResponse;
import com.example.auth.stockPile.model.Notification;
import com.example.auth.stockPile.model.Stock;
import com.example.auth.stockPile.model.Topic;
import com.example.auth.stockPile.model.UserData;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TopicServiceImplTestGenerator {
    private final static String id= "id";
    private final static String userId="id";
    private final static String stockId="id";
    private final static String topicId="id";
    private final static String reactionId="id";
    private final static String commentId="id";
    private final static String postId="id";
    private final static String notificationId="id";
    public static final List<String> subscribers= new ArrayList<>();
    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public static Topic mockTopic(Date date){
        return Topic.builder()
                .id(topicId)
                .title("hello")
                .description("hello")
                .createdBy(mockUserData().getId())
                .createdOn(date)
                .stockId(stockId)
                .stockSymbol("AAPL")
                .stockName("AAPL")
                .build();
    }

    public static List<Topic> mockAllTopic(Date date){
        return List.of( Topic.builder()
                .id(topicId)
                .title("hello")
                .description("hello")
                .createdBy(mockUserData().getId())
                .createdOn(date)
                .stockId(stockId)
                .stockSymbol("AAPL")
                .stockName("AAPL")
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

    public static TopicResponse mockTopicResponse(Date date){
        return TopicResponse.builder()
                .createdBy(mockUserData())
                .createdOn(date)
                .description("hello")
                .stockId(stockId)
                .stockName("AAPL")
                .stockSymbol("AAPL")
                .title("hello")
                .build();
    }

    public static List<TopicResponse> mockAllTopicResponse(Date date){
        return List.of( TopicResponse.builder()
                .createdBy(mockUserData())
                .createdOn(date)
                .description("hello")
                .stockId(stockId)
                .stockName("AAPL")
                .stockSymbol("AAPL")
                .title("hello")
                .build());
    }

    public static Stock mockStock(){
        return Stock.builder()
                .id(stockId)
                .symbol("AAPL")
                .subscribers(subscribers)
                .name("AAPL")
                .description("hello")
                .build();
    }

    public static TopicAddRequest mockTopicAddRequest(){
        return TopicAddRequest.builder()
                .description("hello")
                .title("hello")
                .build();
    }


    public  static Notification mockNotification(){
        return Notification.builder()
                .id(notificationId)
                .userId(userId)
                .deviceToken("fsfp98r9gy94yt9ryge9583y9")
                .build();

    }

    public static Title mockTitle( ){
        return Title.builder()
                .title("hello")
                .createdOn("29-03-2023")
                .build();
    }

}
