package org.example.services;

import org.example.DTOs.RegistrationRequest;
import org.example.DTOs.TelegramMessage;
import org.example.Models.RegistrationSession;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramService {
    private final RegistrationSessionService registrationSessionService;
    private final KafkaTemplate<String, RegistrationRequest> kafkaTemplate;
    private static final String AUTHENTICATION_SERVICE_KAFKA_TOPIC = "telegram-registration";
    private static final String EMAIL_KEY = "email";

    public TelegramService(RegistrationSessionService registrationSessionService, KafkaTemplate<String, RegistrationRequest> kafkaTemplate) {
        this.registrationSessionService = registrationSessionService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String processRegistration (TelegramMessage message) {
        Long chatId = message.message().chat().id();
        String userMessage = message.message().text();
        RegistrationSession session = registrationSessionService.getSession(chatId);

        switch (session.getState()) {
            case INITIAL -> {
                session.setWaitingForEmailState();
                return "Enter your email address";
            }
            case WAITING_FOR_EMAIL -> {
                session.addMessage(EMAIL_KEY, userMessage);
                session.setWaitingForPasswordState();
                return "Enter your password";
            }
            case WAITING_FOR_PASSWORD -> {
                RegistrationRequest registrationRequest = new RegistrationRequest(
                        session.getMessages().get(EMAIL_KEY),
                        userMessage,
                        message.message().user().id()
                );

                kafkaTemplate.send(AUTHENTICATION_SERVICE_KAFKA_TOPIC, registrationRequest);

                registrationSessionService.endSession(chatId);
                return "Ok";
            }
            default -> {
                return "Unexpected state";
            }
        }
    }
}
