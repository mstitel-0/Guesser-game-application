package com.example.authenticationservice.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, String confirmationToken) {
        String confirmationLink = "http://localhost:8080/api/authentication/confirm?token=" + confirmationToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mstitelyarik@gmail.com");
        message.setTo(email);
        message.setSubject("Mail confirmation");
        message.setText("Please, follow this link to confirm your account.\n" + confirmationLink);
        mailSender.send(message);
    }
}
