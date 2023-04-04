package com.example.auth.model;

import com.amazonaws.services.dynamodbv2.xspec.B;
import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.ImageUrl;
import com.example.auth.decorator.SocialVerify;
import com.example.auth.stockPile.model.ServiceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customer")
@Builder
public class
Customer {
    @Id
    String id;

    Role role;

    String name;

    String userName;

    String contact;

    String email;

    String password;

     Date date;

     String otp;


    Map<ImageUrl, String> imageUrl = new HashMap<>();

// Map<ServiceType, Boolean> serviceType = new HashMap<>();

 Map<SocialVerify, Boolean> socialVerify = new HashMap<>();

     Date  otpSendtime;

     Date loginTime;

     Date logoutTime;

   @JsonIgnore
    boolean login;

    @JsonIgnore
    boolean softDelete = false;



}
