package com.example.auth.stockPile.decorator;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicFilter {
    String search;
    Set<String> ids;

    @JsonIgnore
    boolean softDelete;

    public String getSearch(){
        if (search!=null){
            return search;
        }

        return search;
    }

}
