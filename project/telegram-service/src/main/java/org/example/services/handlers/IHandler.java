package org.example.services.handlers;

import org.example.DTOs.TelegramMessage;
import org.example.Models.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface IHandler {
    SendMessage process(TelegramMessage message, UserSession userSession);
}
