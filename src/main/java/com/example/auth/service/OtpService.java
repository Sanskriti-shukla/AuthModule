package com.example.auth.service;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    public String generateOtp() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return Integer.toString(otp);
    }

    public void sendOtp(String email, String otp) throws AddressException, MessagingException {
        // Send the OTP to the specified email address
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nilusroy0@gmail.com");
        message.setTo("sanskrityshukla4@gmail.com");
        message.setSubject("Test Email");
        message.setText("This is a test email");
        javaMailSender.send(message);
    }
    }
