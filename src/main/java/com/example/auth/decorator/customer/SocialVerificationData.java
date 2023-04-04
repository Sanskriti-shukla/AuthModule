package com.example.auth.decorator.customer;

import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.ImageUrl;
import com.example.auth.decorator.SocialVerify;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialVerificationData {
    String id;

    Role role;

    String name;

    String userName;

    String contact;

    String email;

    String password;

    Date date;

    String token;

    String otp;

    Map<ImageUrl, String> imageUrl = new HashMap<>();
Map<SocialVerify, Boolean> socialVerify = new HashMap<>();

}
