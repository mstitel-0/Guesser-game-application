package org.example.services.handlers;

import org.example.Models.GameGuessResponseDTO;
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
public class GuessHandler implements IHandler {

    private final RedisService redisService;
    private final RestClient restClient;
    private final UserSessionManager userSessionManager;

    public GuessHandler(RedisService redisService, RestClient restClient, UserSessionManager userSessionManager) {
        this.redisService = redisService;
        this.restClient = restClient;
        this.userSessionManager = userSessionManager;
    }

    @Override
    public SendMessage process(Update update, UserSession session, Long chatId) {
        System.out.println("Accessed Guess handler");
        SendMessage sendMessage = new SendMessage();
        String userMessage = update.getMessage().getText();

        switch (session.getState()) {
            case INITIAL -> {
                session.setState(SessionState.WAITING_FOR_GUESS);
                session.setExecutingCommand("Guess");

                sendMessage.setText("Enter your guess");
            }
            case WAITING_FOR_GUESS -> {
                String gameId = redisService.getGameId(update.getMessage().getFrom().getId());

                ResponseEntity<GameGuessResponseDTO> response = restClient.post()
                        .uri("/game/" + gameId + "/guess")
                        .body(userMessage)
                        .retrieve()
                        .toEntity(GameGuessResponseDTO.class);


                if (response.getBody().status().equals("WON")) {
                    redisService.removeGameID(update.getMessage().getFrom().getId());
                }
                String hint = response.getBody().hint();
                sendMessage.setText(response.getBody().message() + (hint == null? "" : ". Hint: " + hint));

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
}
