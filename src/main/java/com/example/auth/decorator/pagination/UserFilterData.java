package com.example.auth.decorator.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFilterData {
    String search;
    Set<String> id;

    @JsonIgnore
    boolean softDelete = false;

    public String getSearch() {
        if (search != null) {
            return search.trim();
        }
        return search;
    }

}
