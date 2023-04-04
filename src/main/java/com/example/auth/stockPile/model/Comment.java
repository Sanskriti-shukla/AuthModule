package com.example.auth.stockPile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
@Builder
public class Comment {
    @Id
    String id;
    String comment;
    String commentId;
    String commentedBy;
    Date createdOn;
    String post;

    @JsonIgnore
    boolean softDelete;
}
