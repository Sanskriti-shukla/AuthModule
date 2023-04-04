package com.example.auth.stockPile.repository;

import com.example.auth.stockPile.model.Subscriber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SubscriberRepository extends MongoRepository<Subscriber,String> {

    void deleteByStockidAndUserId(String id, String subscribesId);

    List<Subscriber> findAllByUserId(String userId);
}
