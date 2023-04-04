package com.example.auth.decorator;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
public enum ImageUrl {
    GOOGLE_IMG_URL("googleImageUrl"),
    FACEBOOK_IMG_URL("facebookImageUrl");

     String value;

    ImageUrl(String value) {
        this.value = value;
    }
}
