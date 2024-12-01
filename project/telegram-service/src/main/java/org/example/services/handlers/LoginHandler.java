package org.example.services.handlers;

import org.example.DTOs.LoginRequest;
import org.example.Models.UserSession;
import org.example.Models.enums.SessionState;
import org.example.services.RedisService;
import org.example.services.UserSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class LoginHandler implements IHandler {

    private final UserSessionManager userSessionManager;
    private final RedisService redisService;
    private final RestClient restClient;
    private static final String EMAIL_KEY = "email";


    public LoginHandler(UserSessionManager userSessionManager, RedisService redisService, RestClient restClient) {
        this.userSessionManager = userSessionManager;
        this.redisService = redisService;
        this.restClient = restClient;
    }


    @Override
    public SendMessage process(Update update, UserSession session, Long chatId) {
        System.out.println("Accessed Login handler");
        SendMessage sendMessage = new SendMessage();
        String userMessage = update.getMessage().getText();

        switch (session.getState()) {
            case INITIAL -> {
                session.setState(SessionState.WAITING_FOR_EMAIL);
                session.setExecutingCommand("Login");

                sendMessage.setText("Enter email");
            }
            case WAITING_FOR_EMAIL -> {
                session.addMessage(EMAIL_KEY, userMessage);
                session.setState(SessionState.WAITING_FOR_PASSWORD);

                sendMessage.setText("Enter your password");
            }
            case WAITING_FOR_PASSWORD -> {
                LoginRequest loginRequest = new LoginRequest(
                        session.getMessages().get(EMAIL_KEY),
                        userMessage
                );

                try{
                    ResponseEntity<String> response = restClient.post()
                            .uri("/auth/login")
                            .body(loginRequest)
                            .retrieve()
                            .toEntity(String.class);

                    redisService.saveToken(update.getMessage().getFrom().getId(),
                            response.getBody());

                    sendMessage.setText("Login successful");
                } catch (HttpClientErrorException e) {
                    sendMessage.setText("Error: " + e.getResponseBodyAsString());
                }

                userSessionManager.endSession(chatId);
            }
            default -> {
                userSessionManager.endSession(chatId);
                sendMessage.setText("Unexpected state. Please start login again with Login command.");
            }
        }
        sendMessage.setChatId(chatId);

        return sendMessage;
    }

}
