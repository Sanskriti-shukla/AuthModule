package com.example.auth.stockPile.decorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationAddRequest {
    private String title;
    private String message;
    private String topic;
    private String token;
}
