package org.example.services.handlers;

import org.example.DTOs.RegistrationRequest;
import org.example.Models.UserSession;
import org.example.Models.enums.SessionState;
import org.example.services.UserSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RegistrationHandler implements IHandler {

    private final UserSessionManager userSessionManager;
    private final RestClient restClient;

    private static final String EMAIL_KEY = "email";


    public RegistrationHandler(UserSessionManager userSessionManager, RestClient restClient) {
        this.userSessionManager = userSessionManager;
        this.restClient = restClient;
    }

    @Override
    public SendMessage process(Update update, UserSession session, Long chatId) {
        System.out.println("Accessed Register handler");
        SendMessage sendMessage = new SendMessage();
        String userMessage = update.getMessage().getText();

        switch (session.getState()) {
            case INITIAL -> {
                session.setState(SessionState.WAITING_FOR_EMAIL);
                session.setExecutingCommand("Register");
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
                        update.getMessage().getFrom().getId()
                );

                System.out.println("Created registration request: " + registrationRequest);

                try{
                    ResponseEntity<String> response = restClient.post()
                            .uri("/auth/signup")
                            .body(registrationRequest)
                            .retrieve()
                            .toEntity(String.class);

                    sendMessage.setText("Ok");
                } catch (HttpClientErrorException e){
                    sendMessage.setText("Error: " + e.getResponseBodyAsString());
                }
                userSessionManager.endSession(chatId);
            }
            default -> {
                userSessionManager.endSession(chatId);
                sendMessage.setText("Unexpected state. Please start registration again with Register command.");
            }

        }
        sendMessage.setChatId(chatId);

        return sendMessage;
    }
}
