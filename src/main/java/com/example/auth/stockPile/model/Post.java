package com.example.auth.stockPile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "posts")
@Builder
public class Post {
    @Id
    String id;

    String title;

    String templateContent;

    String postBy;
//    UserData postBy;

    Date createdOn;

    String stockInfo;

    String topicInfo;

    int comments;

    Map<ReactionType,Integer> reaction;

    @JsonIgnore
    boolean softDelete;


}
