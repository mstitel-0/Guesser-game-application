package org.example.services;

import org.example.services.handlers.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HandlerManager {
    private final static Map<String, IHandler> handlers = new HashMap<>();

    public HandlerManager(List<IHandler> handlersList) {
       handlers.put("Register", handlersList
               .stream()
               .filter(handler -> handler instanceof RegistrationHandler)
               .findFirst()
               .orElse(null));

        handlers.put("Login", handlersList
                .stream()
                .filter(handler -> handler instanceof LoginHandler)
                .findFirst()
                .orElse(null));

        handlers.put("/start", handlersList
                .stream()
                .filter(handler -> handler instanceof StartHandler)
                .findFirst()
                .orElse(null));

        handlers.put("Start Game", handlersList
                .stream()
                .filter(handler -> handler instanceof StartGameHandler)
                .findFirst()
                .orElse(null));

        handlers.put("Guess", handlersList
                .stream()
                .filter(handler -> handler instanceof GuessHandler)
                .findFirst()
                .orElse(null));
    }

    public IHandler getHandler(String command) {
        return handlers.get(command);
    }

}
