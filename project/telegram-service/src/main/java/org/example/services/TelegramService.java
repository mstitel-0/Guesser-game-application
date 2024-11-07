package org.example.services;

import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.example.exceptions.UnexpectedCommandException;
import org.example.services.handlers.IHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Service
public class TelegramService {

    private final HandlerManager handlerManager;
    private final UserSessionManager userSessionManager;

    private final RestClient restClient;

    public TelegramService(HandlerManager handlerManager, UserSessionManager userSessionManager, RestClient restClient) {
        this.handlerManager = handlerManager;
        this.userSessionManager = userSessionManager;
        this.restClient = restClient;
    }

    public void processRequest (TelegramMessage message) {
        Long chatId = message.message().chat().id();
        UserSession session = userSessionManager.getSession(chatId);
        String command = extractCommand(message, session);

        System.out.println(command + "- got this");
        IHandler handler = handlerManager.getHandler(command);
        if (!isValidHandler(handler)) {
            sendErrorMessage(command, chatId);
            throw new UnexpectedCommandException(command);
        }


        sendMessage(handler.process(message, session));
    }

    public String extractCommand(TelegramMessage message, UserSession userSession) {
        String command = userSession.getExecutingCommand();
        System.out.println(userSession + " USER SESSIOn");
        return command == null? message.message().text().split(" ")[0] : null;
    }

    public boolean isValidHandler(IHandler handler) {
        return handler != null;
    }

    public void sendErrorMessage(String providedCommand, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Invalid provided command: " + providedCommand);
        sendMessage(sendMessage);
    }

    public void sendMessage(SendMessage message) {
        restClient.post()
                .uri("/sendMessage")
                .body(message)
                .retrieve()
                .toBodilessEntity();
    }
}
