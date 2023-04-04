package com.example.auth.stockPile.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor

public enum Subscribe {
    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unSubscribe");


    private String value;
    Subscribe(String value) {
        this.value=value;
    }
}
