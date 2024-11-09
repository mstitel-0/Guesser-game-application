package org.example.services.handlers;

import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartHandler implements IHandler {
    @Override
    public SendMessage process(Update update, UserSession session, Long chatId) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Register");
        row1.add("Login");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Start Game");
        row2.add("Guess");

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboard.setKeyboard(keyboard);

        SendMessage response = new SendMessage();
        response.setChatId(update.getMessage().getChatId());
        response.setText("Please choose an option:");
        response.setReplyMarkup(replyKeyboard);

        return response;
    }
}
