package com.example.auth.stockPile.repository;

import com.example.auth.commons.decorator.CustomAggregationOperation;
import com.example.auth.decorator.pagination.CountQueryResult;
import com.example.auth.decorator.pagination.FilterSortRequest;

import com.example.auth.decorator.user.UserResponse;
import com.example.auth.stockPile.decorator.StockFilter;
import com.example.auth.stockPile.decorator.StockResponse;
import com.example.auth.stockPile.decorator.StockSortBy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class StockCustomRepositoryImpl implements StockCustomRepository{

        @Autowired
       MongoTemplate mongoTemplate;


    @Override
    public Page<StockResponse> getAllStockByPagination(StockFilter filter, FilterSortRequest.SortRequest<StockSortBy> sort, PageRequest pagination) {
        List<AggregationOperation> operations = userFilterAggregation(filter, sort, pagination, true);

        //Created Aggregation operation
        Aggregation aggregation = newAggregation(operations);


        List<StockResponse> users = mongoTemplate.aggregate(aggregation, "stock_Info", StockResponse.class).getMappedResults();


        // Find Count
        List<AggregationOperation> operationForCount = userFilterAggregation(filter, sort, pagination, false);

        operationForCount.add(group().count().as("count"));
        operationForCount.add(project("count"));
        Aggregation aggregationCount = newAggregation(StockResponse.class, operationForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationCount, "stock_Info", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().size() == 0 ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
                users,
                pagination,
                () -> count);

    }


    Criteria getCriteria(StockFilter stockFilter, List<AggregationOperation> operations) {
        Criteria criteria = new Criteria();
        operations.add(new CustomAggregationOperation(new Document("$addFields", new Document("search",
                new Document("$concat", Arrays.asList(new Document("$ifNull", Arrays.asList("$symbol", "")),
                        "|@|", new Document("$ifNull", Arrays.asList("$name", ""))


                ))))));
        if (!StringUtils.isEmpty(stockFilter.getSearch())) {
            stockFilter.setSearch(stockFilter.getSearch().replaceAll("\\|@\\|", ""));
            stockFilter.setSearch(stockFilter.getSearch().replaceAll("\\|@@\\|", ""));
            criteria = criteria.orOperator(
                    Criteria.where("search").regex(".*" + stockFilter.getSearch() + ".*", "i")
            );
        }
        if (!CollectionUtils.isEmpty(stockFilter.getId())) {
            criteria = criteria.and("_id").is(stockFilter.getId());
        }
        criteria = criteria.and("softDelete").is(false);
        return criteria;
    }

    private List<AggregationOperation> userFilterAggregation(StockFilter filter, FilterSortRequest.SortRequest<StockSortBy> sort, PageRequest pagination, boolean addPage) {
        List<AggregationOperation> operations = new ArrayList<>();

        operations.add(match(getCriteria(filter, operations)));
        if (addPage) {
            //sorting
            if (sort != null && sort.getSortBy() != null && sort.getOrderBy() != null) {
                operations.add(new SortOperation(Sort.by(sort.getOrderBy(), sort.getSortBy().getValue())));
            }
            if (pagination != null) {
                operations.add(skip(pagination.getOffset()));
                operations.add(limit(pagination.getPageSize()));

            }
        }
        return operations;
    }}
