package com.example.auth.decorator.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

}
