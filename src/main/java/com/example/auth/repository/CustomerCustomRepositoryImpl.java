package com.example.auth.repository;

import com.example.auth.commons.decorator.CustomAggregationOperation;
import com.example.auth.decorator.pagination.CountQueryResult;
import com.example.auth.decorator.pagination.CustomerFilter;
import com.example.auth.decorator.pagination.CustomerSortBy;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.model.Customer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
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

@Repository
public class CustomerCustomRepositoryImpl implements CustomerCustomRepository {

    private final MongoTemplate mongoTemplate;

    public CustomerCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Customer> getAllCustomerByPagination(CustomerFilter filter, FilterSortRequest.SortRequest<CustomerSortBy> sort, PageRequest pagination) {
        List<AggregationOperation> operations = filterAggregation(filter, sort, pagination, true);
        Aggregation aggregation = newAggregation(operations);

        List<Customer> customers = mongoTemplate.aggregate(aggregation, "customer", Customer.class).getMappedResults();


        // Find Count
        List<AggregationOperation> operationForCount = filterAggregation(filter, sort, pagination, false);
        operationForCount.add(group().count().as("count"));
        operationForCount.add(project("count"));
        Aggregation aggregationCount = newAggregation(Customer.class, operationForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationCount, "customer", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().size() == 0 ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
                customers,
                pagination,
                () -> count);

    }


    Criteria getCriteria(CustomerFilter filter, List<AggregationOperation> operations) {
        Criteria criteria = new Criteria();
        operations.add(new CustomAggregationOperation(new Document("$addFields", new Document("search",
                new Document("$concat", Arrays.asList(new Document("$ifNull", Arrays.asList("$name", "")),
                        "|@|", new Document("$ifNull", Arrays.asList("$userName", ""))

                ))))));

        if (!StringUtils.isEmpty(filter.getSearch())) {
            filter.setSearch(filter.getSearch().replaceAll("\\|@\\|", ""));
            filter.setSearch(filter.getSearch().replaceAll("\\|@@\\|", ""));
            criteria = criteria.orOperator(
                    Criteria.where("search").regex(".*" + filter.getSearch() + ".*", "i")
            );
        }
        if (!CollectionUtils.isEmpty(filter.getId())) {
            criteria = criteria.and("id").is(filter.getId());
        }
        criteria = criteria.and("softDelete").is(false);
        return criteria;
    }

    private List<AggregationOperation> filterAggregation(CustomerFilter filter, FilterSortRequest.SortRequest<CustomerSortBy> sort, PageRequest pagination, boolean addPage) {
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
    }


}


