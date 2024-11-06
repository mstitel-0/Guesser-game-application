package org.example.services;

import org.example.Models.RegistrationSession;
import org.example.Models.enums.RegistrationState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationSessionService {
    private static final List<RegistrationSession> sessions = new ArrayList<>();

    public RegistrationSession getSession(Long chatId) {
        return sessions.stream()
                .filter(s -> s.getChatId().equals(chatId))
                .findFirst()
                .orElse(startSession(chatId));
    }
    public void endSession(Long chatId) {
        sessions.removeIf(s -> s.getChatId().equals(chatId));
    }
    public RegistrationSession startSession(Long chatId) {
        RegistrationSession session = new RegistrationSession(chatId, RegistrationState.INITIAL);
        sessions.add(session);
        return session;
    }
}
