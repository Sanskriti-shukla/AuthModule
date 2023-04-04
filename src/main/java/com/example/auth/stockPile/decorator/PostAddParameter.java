package com.example.auth.stockPile.decorator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostAddParameter {
    String topicId;
    String stockId;
    String userId;
    PostAddRequest postAddRequest;
}
