package org.example.services.handlers;

import org.example.Models.UserSession;
import org.example.Models.enums.SessionState;
import org.example.services.RedisService;
import org.example.services.UserSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartGameHandler implements IHandler {
    private final UserSessionManager userSessionManager;
    private final RestClient restClient;
    private final RedisService redisService;

    public StartGameHandler(UserSessionManager userSessionManager, RestClient restClient, RedisService redisService) {
        this.userSessionManager = userSessionManager;
        this.restClient = restClient;
        this.redisService = redisService;
    }

    @Override
    public SendMessage process(Update update, UserSession session, Long chatId) {
        System.out.println("Accessed Start Game handler");
        SendMessage sendMessage = new SendMessage();
        String userMessage = update.getMessage().getText();

        switch (session.getState()){
            case INITIAL -> {
                //if no active game in redis
                session.setState(SessionState.WAITING_FOR_TOPIC);
                session.setExecutingCommand("Start Game");

                sendMessage.setText("Choose one ont he following topics:\n1.Animals\n2.Cars\n3.Food");
            }
            case WAITING_FOR_TOPIC -> {
                Long userId = update.getMessage().getFrom().getId();
                String token = redisService.getToken(userId);
                ResponseEntity<String> response = restClient.post()
                        .uri("/game/start")
                        .header("Authorization", "Bearer " + token)
                        .body(userMessage)
                        .retrieve()
                        .toEntity(String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    getGame(sendMessage, userId, token);
                    sendMessage.setText("Game started: " + response.getBody());
                } else {
                    sendMessage.setText("Game start failed");
                }
                userSessionManager.endSession(chatId);
            }
            default -> {
                userSessionManager.endSession(chatId);
                sendMessage.setText("Unexpected state. Please start game again with Start Game command.");
            }
        }



        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    private void getGame(SendMessage sendMessage, Long userId ,String token) {
        ResponseEntity<Long> gameAnswer = restClient.get()
                .uri("game/games/recent")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toEntity(Long.class);

        if (gameAnswer.getStatusCode().is2xxSuccessful()) {
            redisService.saveGame(userId, gameAnswer.getBody().toString());
        } else {
            sendMessage.setText("Game retrieval failed");
        }
    }
}
