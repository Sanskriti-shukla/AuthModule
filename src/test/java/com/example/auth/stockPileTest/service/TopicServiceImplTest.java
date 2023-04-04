package com.example.auth.stockPileTest.service;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.stockPile.model.Notification;
import com.example.auth.stockPile.repository.NotificationRepository;
import com.example.auth.stockPile.repository.TopicRepository;
import com.example.auth.stockPile.service.StockServiceImpl;
import com.example.auth.stockPile.service.TopicService;
import com.example.auth.stockPile.service.TopicServiceImpl;
import com.example.auth.stockPile.service.UserDataServiceImpl;
import com.example.auth.stockPileTest.helper.TopicServiceImplTestGenerator;
import com.example.pushNotfication.FcmService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

 class TopicServiceImplTest {
    private final static String id= "id";
    private final static String userId="id";
    private final static String stockId="id";
    private final static String topicId="id";
    private final static String reactionId="id";
    private final static String commentId="id";
    private final static String postId="id";
    private final static String notificationId="id";
    public static final List<String> subscribers= new ArrayList<>();
    private final TopicRepository topicRepository = mock(TopicRepository.class);
    private final ModelMapper modelMapper = TopicServiceImplTestGenerator.getModelMapper();
    private final StockServiceImpl stockService = mock(StockServiceImpl.class);
    private final UserDataServiceImpl userDataService = mock(UserDataServiceImpl.class);
    private final UserHelper userHelper = mock(UserHelper.class);
    private final FcmService fcmService = mock(FcmService.class);
    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);


    public TopicService topicService = new TopicServiceImpl(topicRepository,modelMapper,stockService,userDataService,userHelper,fcmService,notificationRepository);
    @Test

    void testAddTopic(){
        Date date= new Date();
        var stock = TopicServiceImplTestGenerator.mockStock();

         var userData = TopicServiceImplTestGenerator.mockUserData();

         var topic = TopicServiceImplTestGenerator.mockTopic(date);

         var topicResponse = TopicServiceImplTestGenerator.mockTopicResponse(date);

         var topicAddRequest = TopicServiceImplTestGenerator.mockTopicAddRequest();

         var notification = TopicServiceImplTestGenerator.mockNotification();

         when(stockService.stockById(stockId)).thenReturn(stock);

         when(userDataService.userById(userId)).thenReturn(userData);

         when(topicRepository.save(topic)).thenReturn(topic);

         var actualData = topicService.addTopic(stockId,userId,topicAddRequest);

        Assertions.assertEquals(topicResponse.getStockName(),actualData.getStockName());
    }

     @Test

    void testUpdateTopic() throws NoSuchFieldException, IllegalAccessException {
         Date date= new Date();

         var topic = TopicServiceImplTestGenerator.mockTopic(date);

         var topicAddRequest = TopicServiceImplTestGenerator.mockTopicAddRequest();

         when(topicRepository.getTopicByIdAndSoftDeleteIsFalse(topicId)).thenReturn(java.util.Optional.ofNullable(topic));

          topicService.updateTopic(topicId,topicAddRequest);

         verify(topicRepository,times(2)).getTopicByIdAndSoftDeleteIsFalse(topicId);
     }


     @Test
    void testGetTopicById(){
         Date date= new Date();

         var topic = TopicServiceImplTestGenerator.mockTopic(date);

         var topicResponse = TopicServiceImplTestGenerator.mockTopicResponse(date);

         when(topicRepository.getTopicByIdAndSoftDeleteIsFalse(topicId)).thenReturn(java.util.Optional.ofNullable(topic));

         var actualData = topicService.getTopicById(topicId);

         Assertions.assertEquals(topicResponse,actualData);
     }

    @Test
    void testGetAllTopic(){
        Date date= new Date();

        var topicResponses = TopicServiceImplTestGenerator.mockAllTopicResponse(date);

        var topic = TopicServiceImplTestGenerator.mockTopic(date);

        var topics = TopicServiceImplTestGenerator.mockAllTopic(date);

        when(topicRepository.findAllBySoftDeleteFalse()).thenReturn(topics);

        var actualData = topicService.getAllTopic();

        Assertions.assertEquals(topicResponses,actualData);
    }

    @Test
    void testDeleteTopicById(){
        Date date= new Date();
        var topic = TopicServiceImplTestGenerator.mockTopic(date);

        when(topicRepository.getTopicByIdAndSoftDeleteIsFalse(topicId)).thenReturn(java.util.Optional.ofNullable(topic));

         topicService.deleteTopicById(topicId);

         verify(topicRepository,times(1)).getTopicByIdAndSoftDeleteIsFalse(topicId);
    }

}
