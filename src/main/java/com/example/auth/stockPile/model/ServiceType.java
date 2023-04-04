package com.example.auth.stockPile.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
public enum ServiceType {
    GOOGLE("Google"),
    FACEBOOK("Facebook"),
    NORMAL("Normal");

    private  String value;

    ServiceType(String label) {
        this.value = label;
    }

}