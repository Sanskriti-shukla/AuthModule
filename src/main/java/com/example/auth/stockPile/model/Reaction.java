package com.example.auth.stockPile.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reaction")
@Builder
public class Reaction {

    @Id
    String id;

    String userId;

    String postId;

    ReactionType reactionType;

    @JsonIgnore
    boolean softDelete;
}
