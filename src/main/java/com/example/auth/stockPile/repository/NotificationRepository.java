package com.example.auth.stockPile.repository;

import com.example.auth.stockPile.model.Notification;
import com.example.auth.stockPile.model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String> {

   Notification findByUserId(String userId);

   List<Notification> findAllByUserId(String userId);

   List<Notification> findAllByUserId(List<String> subscribers);
}
