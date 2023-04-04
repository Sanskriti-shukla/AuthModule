package com.example.auth.stockPile.decorator;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum TopicSortBy {
    DATE("createdOn"),
    TITLE("title");

    @JsonIgnore
    private String value;
    TopicSortBy(String value) {
        this.value=value;
    }
}
