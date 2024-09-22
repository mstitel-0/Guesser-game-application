package com.example.authenticationservice.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderMail;

    private final String CONFIRMATION_LINK = "http://localhost:8080/api/authentication/confirm?token=";

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, String confirmationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(email);
        message.setSubject("Mail confirmation");
        message.setText("Please, follow this link to confirm your account.\n" + CONFIRMATION_LINK + confirmationToken);
        mailSender.send(message);
    }
}
