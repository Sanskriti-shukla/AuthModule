package com.example.auth.model;

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
@Builder
@Document(collection = "purchaseLogHistory")
public class PurchaseLogHistory  {
    @Id
    String id;

    String customerId;

    String customerName;

    String itemName;

    double price;

    int quantity;

    double discountInPercent;

    double discountInRupee;

    double totalPrice;

    Date date;
    double total;
    @JsonIgnore
    boolean softDelete;

}


