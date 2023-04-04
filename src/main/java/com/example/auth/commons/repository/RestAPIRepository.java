package com.example.auth.commons.repository;

import com.example.auth.commons.model.RestAPI;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RestAPIRepository extends MongoRepository<RestAPI,String> {

    List<RestAPI> findAllBy();

  boolean existsByRolesInAndName( List<String> roles,String name);

    boolean existsByName(String name);

}
