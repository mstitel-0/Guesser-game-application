package org.example.Models;

import org.example.Models.enums.RegistrationState;

import java.util.HashMap;
import java.util.Map;

public class RegistrationSession {
    private Long chatId;
    private Map<String, String> messages;
    private RegistrationState state;

    public RegistrationSession(Long chatId, RegistrationState state) {
        this.chatId = chatId;
        this.state = state;
        this.messages = new HashMap<>();
    }

    public void setWaitingForEmailState() {
        this.state = RegistrationState.WAITING_FOR_EMAIL;
    }
    public void setWaitingForPasswordState() {
        this.state = RegistrationState.WAITING_FOR_PASSWORD;
    }
    public void setFinishedState() {
        this.state = RegistrationState.FINISHED;
    }

    public Long getChatId() {
        return chatId;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public RegistrationState getState() {
        return state;
    }

    public void addMessage(String key, String message){
        messages.put(key, message);
    }
}
