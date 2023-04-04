package com.example.auth.repository;

import com.example.auth.commons.decorator.CustomAggregationOperation;
import com.example.auth.decorator.category.CategoryResponse;
import com.example.auth.decorator.pagination.CategoryFilter;
import com.example.auth.decorator.pagination.CategorySortBy;
import com.example.auth.decorator.pagination.CountQueryResult;
import com.example.auth.decorator.pagination.FilterSortRequest;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<CategoryResponse> getAllCategoryByPagination(CategoryFilter filter, FilterSortRequest.SortRequest<CategorySortBy> sort, PageRequest pageRequest) {
        List<AggregationOperation> operations = categoryAggregationFilter(filter, sort, pageRequest, true);

        Aggregation aggregation = newAggregation(operations);
        List<CategoryResponse> category = mongoTemplate.aggregate(aggregation, "categories", CategoryResponse.class).getMappedResults();
//FindCount
        List<AggregationOperation> operationsForCount = categoryAggregationFilter(filter, sort, pageRequest, false);
        operationsForCount.add(group().count().as("count"));
        operationsForCount.add(project("count"));
        Aggregation aggregationForCount = newAggregation(CategoryResponse.class, operationsForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationForCount, "categories", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().size() == 0 ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
                category,
                pageRequest,
                () -> count);
    }



    private List<AggregationOperation> categoryAggregationFilter(CategoryFilter filter, FilterSortRequest.SortRequest<CategorySortBy> sort, PageRequest pageRequest, boolean addPage) {
        List<AggregationOperation> operations = new ArrayList<>();

        operations.add(match(getCriteria(filter, operations)));
        if (addPage) {
            //sorting
            if (sort != null && sort.getSortBy() != null && sort.getOrderBy() != null) {
                operations.add(new SortOperation(Sort.by(sort.getOrderBy(), sort.getSortBy().getValue())));
            }
            if (pageRequest != null) {
                operations.add(skip(pageRequest.getOffset()));
                operations.add(limit(pageRequest.getPageSize()));
            }
        }
        return operations;
    }


    private Criteria getCriteria(CategoryFilter filter, List<AggregationOperation> operations) {
        Criteria criteria = new Criteria();
        operations.add(new CustomAggregationOperation(
                new Document("$addFields",
                        new Document("search",
                                new Document("$concat", Arrays.asList(
                                        new Document("$ifNull", Arrays.asList("$categoryName", ""))
                ))))));

        if (!StringUtils.isEmpty(filter.getSearch())) {
            filter.setSearch(filter.getSearch().replace("\\|@\\|", ""));
            filter.setSearch(filter.getSearch().replace("\\|@@\\|", ""));

            criteria = criteria.orOperator(
                    Criteria.where("search").regex(".*" + filter.getSearch() + ".*", "i")
            );
        }
        if (!CollectionUtils.isEmpty((filter.getId()))){
            criteria=criteria.and("_id").is(filter.getId());
        }
        criteria = criteria.and("softDelete").is(false);

        return criteria;
    }
}




