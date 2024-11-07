package org.example.services.handlers;

import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartHandler implements IHandler {
    @Override
    public SendMessage process(TelegramMessage message, UserSession session) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        InlineKeyboardButton registerButton = new InlineKeyboardButton("Register");
        registerButton.setCallbackData("register");
        InlineKeyboardButton loginButton = new InlineKeyboardButton("Login");
        loginButton.setCallbackData("login");

        InlineKeyboardButton startGameButton = new InlineKeyboardButton("Start Game");
        startGameButton.setCallbackData("start-game");
        InlineKeyboardButton guessGameButton = new InlineKeyboardButton("Guess");
        guessGameButton.setCallbackData("guess");

        List<InlineKeyboardButton> row1 = List.of(registerButton, loginButton);
        List<InlineKeyboardButton> row2 = List.of(startGameButton, guessGameButton);
        keyboard.add(row1);
        keyboard.add(row2);

        inlineKeyboard.setKeyboard(keyboard);

        SendMessage response = new SendMessage();
        response.setChatId(message.message().chat().id());
        response.setText("Please choose an option:");
        response.setReplyMarkup(inlineKeyboard);

        return response;
    }
}
