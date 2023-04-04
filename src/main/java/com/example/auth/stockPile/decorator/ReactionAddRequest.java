package com.example.auth.stockPile.decorator;


import com.example.auth.stockPile.model.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactionAddRequest {

    String userId;

    String postId;
}
