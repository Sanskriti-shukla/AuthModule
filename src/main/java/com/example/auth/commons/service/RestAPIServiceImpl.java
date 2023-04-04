package com.example.auth.commons.service;

import com.example.auth.commons.model.RestAPI;
import com.example.auth.commons.repository.RestAPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@Repository
class RestApiServiceImpl implements RestAPIService {

    @Autowired
    RestAPIRepository restApiRepository;

    @Override
    public List<RestAPI> getAll() {
        return restApiRepository.findAllBy();
    }

    @Override
    public boolean hasAccess(List<String> roles,String name) {
        return restApiRepository.existsByRolesInAndName(roles,name);
    }
}
