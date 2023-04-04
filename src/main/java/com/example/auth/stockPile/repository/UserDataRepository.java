package com.example.auth.stockPile.repository;


import com.example.auth.stockPile.model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends MongoRepository<UserData, String> {

    Optional<UserData> findByIdAndSoftDeleteIsFalse(String id);

    Optional<UserData> findByEmailAndSoftDeleteIsFalse(String email);

     List<UserData> findByIdAndSoftDeleteFalse(String id);

      List<UserData> findAllBySoftDeleteFalse();

       boolean existsByEmailAndSoftDeleteIsFalse(String email);


       UserData findByEmail(String email);

    Optional<UserData> findByNameAndSoftDeleteIsFalse(String name);
}
