package org.example.authenticationservice.Services;

import org.example.DTOs.MailConfirmationRequest;
import org.example.DTOs.RegistrationRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaTelegramConsumer {
    private final AuthenticationService authenticationService;

    public KafkaTelegramConsumer(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @KafkaListener(topics = "telegram-registration", groupId = "auth-service")
    public void processMessage(RegistrationRequest registrationRequest) {
        authenticationService.register(registrationRequest);
    }
}
