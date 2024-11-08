package org.example.services.handlers;

import org.example.DTOs.RegistrationRequest;
import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.example.Models.enums.SessionState;
import org.example.services.UserSessionManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class RegistrationHandler implements IHandler {

    private final UserSessionManager userSessionManager;
    private final KafkaTemplate<String, RegistrationRequest> kafkaTemplate;

    private static final String AUTHENTICATION_SERVICE_KAFKA_TOPIC = "telegram-registration";
    private static final String EMAIL_KEY = "email";


    public RegistrationHandler(UserSessionManager userSessionManager, KafkaTemplate<String, RegistrationRequest> kafkaTemplate) {
        this.userSessionManager = userSessionManager;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public SendMessage process(TelegramMessage message, UserSession session) {
        SendMessage sendMessage = new SendMessage();
        String userMessage = message.message().text();
        Long chatId = message.message().chat().id();

        switch (session.getState()) {
            case INITIAL -> {
                session.setState(SessionState.WAITING_FOR_EMAIL);
                session.setExecutingCommand("/register");
                sendMessage.setText("Provide your email address");
                
            }
            case WAITING_FOR_EMAIL -> {
                session.addMessage(EMAIL_KEY, userMessage);
                session.setState(SessionState.WAITING_FOR_PASSWORD);

                sendMessage.setText("Enter your password");
            }
            case WAITING_FOR_PASSWORD -> {
                RegistrationRequest registrationRequest = new RegistrationRequest(
                        session.getMessages().get(EMAIL_KEY),
                        userMessage,
                        message.message().user().id()
                );

                kafkaTemplate.send(AUTHENTICATION_SERVICE_KAFKA_TOPIC, registrationRequest);

                userSessionManager.endSession(chatId);
                sendMessage.setText("Ok");
            }
            default -> sendMessage.setText("Unexpected state");

        }
        sendMessage.setChatId(chatId);

        return sendMessage;
    }
}
