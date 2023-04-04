package com.example.auth.stockPile.model;


import com.example.auth.commons.Access;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "stock_info")
@Builder
public class Stock {

    @Id
    String id;

    String symbol;

    List<String> subscribers;

    String name;

    String description;


    @JsonIgnore

    boolean softDelete;

}
