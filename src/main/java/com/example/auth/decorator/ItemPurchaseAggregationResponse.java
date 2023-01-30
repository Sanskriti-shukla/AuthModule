package com.example.auth.decorator;

import com.example.auth.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPurchaseAggregationResponse {
    String _id;
    List<ItemDetail> itemDetail;
}
