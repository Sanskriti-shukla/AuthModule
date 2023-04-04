package com.example.auth.decorator.customer;

import com.example.auth.decorator.ImageUrl;
import com.example.auth.stockPile.model.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAddRequest {
    String name;
    String email;
    String contact;
    String userName;
    String password;
    String confirmPassword;
    String imageUrl;

}
