package com.example.auth.decorator;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public enum SocialVerify  {
    GOOGLE("google"),
    FACEBOOK("facebook");

    private String value;

    SocialVerify(String value) {
        this.value = value;
    }
}
