package org.example.services;

import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.example.exceptions.UnexpectedCommandException;
import org.example.services.handlers.IHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;


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
        List<UserSession> sessions = userSessionManager.getSessions();

        System.out.println("All session");
        for(UserSession session : sessions) {
            System.out.println(session);
        }
        Long chatId = message.message().chat().id();
        UserSession session = userSessionManager.getSession(chatId);

        System.out.println("User session: " + session);
        String command = extractCommand(message, session);
        IHandler handler = handlerManager.getHandler(command);

        if (!isValidHandler(handler)) {
            sendErrorMessage(command, chatId);
            throw new UnexpectedCommandException(command);
        }

        System.out.println("SESSION BEFORE: " + session);
        sendMessage(handler.process(message, session));

        System.out.println("SESSION AFTER: " + session);
    }

    public String extractCommand(TelegramMessage message, UserSession userSession) {
        String command = userSession.getExecutingCommand();
        if(command != null) return command;
        if (message.message().text().split(" ")[0] != null) return message.message().text().split(" ")[0];

        return null;
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
