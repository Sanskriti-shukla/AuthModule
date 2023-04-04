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
@Document(collection = "subscribers")
@Builder
public class Subscriber {
    @Id
    String id;
    String userId;
    String stockid;
    Date createdOn;
    Subscribe subscribe;
    @JsonIgnore
    boolean softDelete;

}
