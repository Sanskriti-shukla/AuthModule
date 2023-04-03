package com.example.auth.stockPile.service;


import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.stockPile.decorator.*;
import com.example.auth.stockPile.model.*;
import com.example.auth.stockPile.repository.NotificationRepository;
import com.example.auth.stockPile.repository.TopicRepository;
import com.example.pushNotfication.FcmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;
    private final StockServiceImpl stockService;
    private final UserDataServiceImpl userDataService;
    private final UserHelper userHelper;
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final Notification notification;


    public TopicServiceImpl(TopicRepository topicRepository, ModelMapper modelMapper, StockServiceImpl stockService, UserDataServiceImpl userDataService, UserHelper userHelper, FcmService fcmService, NotificationRepository notificationRepository, Notification notification) {
        this.topicRepository = topicRepository;
        this.modelMapper = modelMapper;
        this.stockService = stockService;
        this.userDataService = userDataService;
        this.userHelper = userHelper;
        this.fcmService = fcmService;
        this.notificationRepository = notificationRepository;
        this.notification = notification;
    }

    @Override
    public TopicResponse addTopic(String stockId, String userId, TopicAddRequest topicAddRequest) {
        Stock stock = stockService.stockById(stockId);
        UserData userData = userDataService.userById(userId);
        Topic topic = modelMapper.map(topicAddRequest, Topic.class);
        topic.setStockId(stock.getId());
        topic.setStockSymbol(stock.getSymbol());
        topic.setStockName(stock.getName());
        topic.setCreatedOn(new Date());
        topic.setCreatedBy(userData.getId());
        TopicResponse topicResponse = modelMapper.map(topic, TopicResponse.class);
        topicResponse.setCreatedBy(userData);
        topicRepository.save(topic);
        List<String> subscribers = stock.getSubscribers();
        for (String subscriberId : subscribers) {
            if (subscriberId.equals(userId)) {
                // Don't send a notification to the user who created the topic
                continue;
            }
            // Get the notification data for this subscriber
            Notification notification = notificationRepository.findByUserId(subscriberId);
            if (notification == null) {
                // This subscriber has not enabled notifications
                continue;
            }
            // Send a push notification to the subscriber
            String deviceToken = notification.getDeviceToken();
            fcmService.sendPushNotification(new String[]{deviceToken}, "New topic created", "A new topic has been created for " + stock.getName());
        }
        return topicResponse;

    }

    @Override
    public void updateTopic(String id, TopicAddRequest topicAddRequest) throws NoSuchFieldException, IllegalAccessException {
        Topic topic = topicById(id);
        update(id, topicAddRequest);
        userHelper.difference(topic, topicAddRequest);
    }


    @Override
    public Page<TopicResponse> getAllTopicByPagination(TopicFilter filter, FilterSortRequest.SortRequest<TopicSortBy> sort, PageRequest pagination) {

        Page<Topic> topicResponses =  topicRepository.getAllTopicByPagination(filter, sort, pagination);
        List<TopicResponse> list = new ArrayList<>();

        topicResponses.forEach(topic -> {
            TopicResponse topicResponse = modelMapper.map(topic, TopicResponse.class);
            UserData userData = userDataService.userById(topic.getCreatedBy());
            topicResponse.setCreatedBy(userData);
            list.add(topicResponse);
        });
        Page<TopicResponse> page = new PageImpl<>(list, pagination, topicResponses.getTotalElements());
        return page;
    }

    @Override
    public String getTopicIdByTitleAndCreatedOn(Title title) throws ParseException {
        List<Topic> topics = topicRepository.findAllBySoftDeleteFalse();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date parsedCreatedOn = formatter.parse(title.getCreatedOn());
        for (Topic topic : topics) {
            if (topic.getCreatedOn().equals(parsedCreatedOn) && topic.getTitle().equals(title.getTitle())) {
                return topic.getId().toString();
            }
        }
        throw new NotFoundException(MessageConstant.TOPIC_NOT_FOUND);
    }

    @Override
    public TopicResponse getTopicById(String id) {
        Topic topic = topicById(id);
        TopicResponse topicResponse = modelMapper.map(topic, TopicResponse.class);
        UserData userData = userDataService.userById(topic.getCreatedBy());
        topicResponse.setCreatedBy(userData);
        return topicResponse;
    }

    @Override
    public List<TopicResponse> getAllTopic() {
        List<Topic> topics = new ArrayList<>(topicRepository.findAllBySoftDeleteFalse());
        if (!CollectionUtils.isEmpty(topics)) {
            topics.sort(Comparator.comparing(Topic::getCreatedOn).reversed());
        }
        List<TopicResponse> list = new ArrayList<>();
        topics.forEach(topic -> {
            TopicResponse topicResponse = modelMapper.map(topic, TopicResponse.class);
            UserData userData = userDataService.userById(topic.getCreatedBy());
            topicResponse.setCreatedBy(userData);
            list.add(topicResponse);
        });
        return list;
    }


    @Override
    public void deleteTopicById(String id) {
        Topic topic = topicById(id);
        topic.setSoftDelete(true);
        topicRepository.save(topic);

    }

    private void update(String id, TopicAddRequest topicAddRequest) {
        Topic topic = topicById(id);
        if (topicAddRequest.getDescription() != null) {
            topic.setDescription(topicAddRequest.getDescription());
        }
        if (topicAddRequest.getTitle() != null) {
            topic.setTitle(topicAddRequest.getTitle());
        }
    }


    public Topic topicById(String id) {
        return topicRepository.getTopicByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.ID_NOT_FOUND));
    }

}
