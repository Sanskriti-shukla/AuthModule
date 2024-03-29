package com.example.auth.stockPile.repository;

import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.stockPile.decorator.TopicFilter;
import com.example.auth.stockPile.decorator.TopicSortBy;
import com.example.auth.stockPile.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TopicCustomRepository {

    Page<Topic> getAllTopicByPagination(TopicFilter filter, FilterSortRequest.SortRequest<TopicSortBy> sort, PageRequest pagination);
}
