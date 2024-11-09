package org.example.Models;

import org.example.Models.enums.SessionState;

import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private final Long chatId;
    private final Map<String, String> messages;
    private SessionState state;
    private String executingCommand;

    public UserSession(Long chatId, SessionState state) {
        this.chatId = chatId;
        this.state = state;
        this.messages = new HashMap<>();
    }

    public String getExecutingCommand() {
        return executingCommand;
    }

    public void setExecutingCommand(String executingCommand) {
        this.executingCommand = executingCommand;
    }

    public void setState(SessionState state) {
        this.state = state;
    }

    public Long getChatId() {
        return chatId;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public SessionState getState() {
        return state;
    }

    public void addMessage(String key, String message){
        messages.put(key, message);
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "chatId=" + chatId +
                ", messages=" + messages +
                ", state=" + state +
                ", executingCommand='" + executingCommand + '\'' +
                '}';
    }
}
