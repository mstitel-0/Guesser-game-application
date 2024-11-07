package org.example.services.handlers;

import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class LoginHandler implements IHandler {

    @Override
    public SendMessage process(TelegramMessage message, UserSession session) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.message().chat().id());
        sendMessage.setText("login handler");
        return sendMessage;
    }

}
