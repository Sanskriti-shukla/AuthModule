package com.example.auth.stockPile.decorator;

import com.example.auth.stockPile.model.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationTopicResponse {
    String title;

    String description;

    String stockId;

    String stockSymbol;

    String stockName;

    UserData createdBy;

    Date createdOn;


}
