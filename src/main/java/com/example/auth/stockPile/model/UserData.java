package com.example.auth.stockPile.model;


import com.example.auth.decorator.ImageUrl;
import com.example.auth.decorator.SocialVerify;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor

@NoArgsConstructor

@Document(collection = "userData")
@Builder
public class UserData {

    @Id

    String id;

    String name;

    String userName;

    String email;

    String contact;

    boolean subscribe;

//    String imageurl;
Map<ImageUrl, String> imageUrl = new HashMap<>();
 Map<SocialVerify, Boolean> socialVerify = new HashMap<>();

    @JsonIgnore

    boolean softDelete;
}
