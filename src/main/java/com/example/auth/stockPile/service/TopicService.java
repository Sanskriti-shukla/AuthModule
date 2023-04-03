package com.example.auth.stockPile.service;


import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.stockPile.decorator.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.util.List;

public interface TopicService {

    TopicResponse addTopic( String stockId, String userId,TopicAddRequest topicAddRequest);

    TopicResponse getTopicById(String id);

    List<TopicResponse> getAllTopic();

    void deleteTopicById(String id);

    void updateTopic(String id, TopicAddRequest topicAddRequest) throws NoSuchFieldException, IllegalAccessException;

    Page<TopicResponse> getAllTopicByPagination(TopicFilter filter, FilterSortRequest.SortRequest<TopicSortBy> sort, PageRequest pagination);

   String getTopicIdByTitleAndCreatedOn(Title title) throws ParseException;

}
