package com.example.auth.decorator.customer;

import com.example.auth.decorator.ImageUrl;
import com.example.auth.decorator.SocialVerify;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialVerificationAddRequest {
    String email;
    String imageUrl;


}
