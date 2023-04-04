package com.example.auth.commons.service;

import com.example.auth.commons.model.RestAPI;

import java.util.List;

public interface RestAPIService {

    List<RestAPI> getAll();

    boolean hasAccess(List<String> roles ,String name);

}
