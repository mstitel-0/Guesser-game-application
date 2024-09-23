package com.example.mailservice.Services;

import com.example.mailservice.DTOs.MailConfirmationRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMailConsumer {
    private final MailService mailService;

    public KafkaMailConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(topics = "mail-confirmation", groupId = "mail-service")
    public void processMessage(MailConfirmationRequest request) {
        mailService.sendEmail(request.email(), request.token());
    }
}
