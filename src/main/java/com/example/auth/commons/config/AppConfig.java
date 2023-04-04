package com.example.auth.commons.config;

import com.example.pushNotfication.FcmService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public FcmService fcmService(RestTemplate restTemplate, @Value("${fcm.api.key}") String fcmApiKey) {
        return new FcmService(restTemplate, fcmApiKey);
    }
}