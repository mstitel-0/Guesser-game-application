package com.example.mailservice.Services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMailConsumer {
    private final MailService mailService;

    public KafkaMailConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(topics = "mail-confirmation", groupId = "mail-service")
    public void processMessage(String message) {
        String[] parts = message.split(":");
        String email = parts[0];
        String token = parts[1];

        mailService.sendEmail(email, token);
    }
}
