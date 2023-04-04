package com.example.auth.stockPile.decorator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {

    String id;

    String symbol;

    String name;

    String description;

    List<String> subscribers;
}
