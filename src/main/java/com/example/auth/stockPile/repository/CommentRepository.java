package com.example.auth.stockPile.repository;


import com.example.auth.stockPile.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {

    Optional<Comment> getByIdAndSoftDeleteIsFalse(String id);

    List<Comment> findAllBySoftDeleteFalse();

    List<Comment> findAllByPostAndSoftDeleteIsFalse(String postId);
}
