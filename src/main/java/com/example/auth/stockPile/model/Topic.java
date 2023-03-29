package com.example.auth.stockPile.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "topics")
@Builder
public class Topic {

    String id;

    String title;

    String description;

    String stockId;

    String stockSymbol;

    String stockName;

    UserData createdBy;

    Date createdOn;

    @JsonIgnore

    boolean softDelete;

}
