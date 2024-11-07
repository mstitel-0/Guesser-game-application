package org.example.services;

import org.example.Models.UserSession;
import org.example.Models.enums.SessionState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSessionManager {
    private static final List<UserSession> sessions = new ArrayList<>();

    public UserSession getSession(Long chatId) {
        return sessions.stream()
                .filter(s -> s.getChatId().equals(chatId))
                .findFirst()
                .orElse(new UserSession(chatId, SessionState.INITIAL));
    }

    public void endSession(Long chatId) {
        sessions.removeIf(s -> s.getChatId().equals(chatId));
    }

}
