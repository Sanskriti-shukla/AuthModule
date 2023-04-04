package com.example.auth.stockPile.decorator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockSubscribed {
    String stockId;
    String symbol;
    String name;
}
