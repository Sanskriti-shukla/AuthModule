package com.example.auth.stockPile.repository;

import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.stockPile.decorator.PostFilter;
import com.example.auth.stockPile.decorator.PostSortBy;
import com.example.auth.stockPile.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PostCustomRepository {

    Page<Post> getAllPostByPagination(PostFilter filter, FilterSortRequest.SortRequest<PostSortBy> sort, PageRequest pagination);

}
