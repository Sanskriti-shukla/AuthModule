package com.example.auth.stockPile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_deviceToken")
@Builder
public class Notification {
    @Id
    String id;
    String userId;
    String deviceToken;

}
