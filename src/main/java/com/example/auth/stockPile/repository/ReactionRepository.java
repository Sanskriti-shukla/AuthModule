package com.example.auth.stockPile.repository;

import com.example.auth.stockPile.model.Reaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReactionRepository extends MongoRepository<Reaction,String> {
    Reaction findByPostIdAndUserId(String id, String id1);
    List<Reaction> findAllByPostIdAndSoftDeleteIsFalse(String postId);
}
