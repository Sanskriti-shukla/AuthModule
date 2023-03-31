package com.example.auth.stockPile.decorator;

import com.example.auth.stockPile.model.ReactionType;
import com.example.auth.stockPile.model.UserData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    String id;

    String title;

    String templateContent;

    UserData postBy;

    Date createdOn;

    String stockInfo;

    String topicInfo;

    int comments;

    Map<ReactionType,Integer> reaction;

}
