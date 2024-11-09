package org.example.services;

import org.example.Models.UserSession;
import org.example.services.handlers.IHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public class TelegramBot extends SpringWebhookBot {

    private final HandlerManager handlerManager;
    private final UserSessionManager userSessionManager;
    private final String botUsername;

    public TelegramBot(SetWebhook setWebhook, String botToken, String botUsername, HandlerManager handlerManager, UserSessionManager userSessionManager) {
        super(setWebhook, botToken);
        this.botUsername = botUsername;
        this.handlerManager = handlerManager;
        this.userSessionManager = userSessionManager;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println("Accessed onWebHookUpdateReceived");
        Long chatId = update.getMessage().getChatId();
        UserSession session = userSessionManager.getSession(chatId);

        String command = extractCommand(update.getMessage(), session);

        IHandler handler = handlerManager.getHandler(command);

        if (!isValidHandler(handler)) {
            return new SendMessage(chatId.toString(),
                    String.format("Unexpected command: %s\nType /help for available commands", command));
        }

        System.out.println("Passed to handler");
        SendMessage message = handler.process(update, session, chatId);
        System.out.println("Processed message: " + message);

        return message;
    }


    public String extractCommand(Message message, UserSession userSession) {
        String sessionCommand = userSession.getExecutingCommand();
        if(sessionCommand != null) {
            return sessionCommand;
        }
        return message.getText();
    }

    public boolean isValidHandler(IHandler handler) {
        return handler != null;
    }

    @Override
    public String getBotPath() {
        return this.getSetWebhook().getUrl();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
