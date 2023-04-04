package com.example.pushNotfication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FcmService {

    private static final String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String FCM_CONTENT_TYPE = "application/json";

    private final RestTemplate restTemplate;
    private final String fcmApiKey;

    public FcmService(RestTemplate restTemplate, @Value("${fcm.api.key}") String fcmApiKey) {
        this.restTemplate = restTemplate;
        this.fcmApiKey = fcmApiKey;
    }
    public void sendPushNotification(String[] tokens, String title, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(fcmApiKey);

        List<String> tokenList = new ArrayList<>();
        for (String token : tokens) {
            tokenList.add(token);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("registration_ids", tokenList);


        Map<String, String> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);

        payload.put("notification", notification);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(FCM_API_URL, request, Void.class);
    }


}