package com.example.auth.repository;


import com.example.auth.model.User;
import com.example.auth.repository.UserCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String>, UserCustomRepository {

    Optional<User> findByIdAndSoftDeleteIsFalse(String id);


    List<User> findAllBySoftDeleteFalse();

}
