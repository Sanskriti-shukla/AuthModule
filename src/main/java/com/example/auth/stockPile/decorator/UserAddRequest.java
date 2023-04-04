package com.example.auth.stockPile.decorator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddRequest {

    String name;

    String userName;

    String email;

    String contact;


}
