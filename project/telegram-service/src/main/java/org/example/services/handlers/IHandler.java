package org.example.services.handlers;

import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IHandler {
    SendMessage process(Update update, UserSession userSession, Long chatId);
}
