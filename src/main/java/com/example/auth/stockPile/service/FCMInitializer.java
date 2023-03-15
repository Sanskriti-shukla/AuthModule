//package com.example.auth.stockPile.service;
//
//import com.google.api.client.util.Value;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//
//
//@Service
//public class FCMInitializer {
//    @Value("${app.firebase-configuration.file}")
//    private String firebaseConfigPath;
//    Logger logger= LoggerFactory.getLogger(FCMInitializer.class);
//    @PostConstruct
//    public void initialize(){
//        try{
//            FirebaseOptions firebaseOptions= FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
//                    .build();
//            if (FirebaseApp.getApps().isEmpty()){
//                FirebaseApp.initializeApp(firebaseOptions);
//                System.out.println("firebase application has been started");
//            }
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//}
