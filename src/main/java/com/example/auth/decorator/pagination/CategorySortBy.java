package com.example.auth.decorator.pagination;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public enum CategorySortBy {
    CATEGORY_NAME("categoryName");

    private String value;

    CategorySortBy(String value) {
        this.value = value;
    }

        Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("value", this.toString());
        return map;
    }
}
