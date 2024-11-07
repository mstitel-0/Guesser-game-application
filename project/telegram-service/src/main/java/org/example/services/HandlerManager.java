package org.example.services;

import org.example.services.handlers.IHandler;
import org.example.services.handlers.LoginHandler;
import org.example.services.handlers.RegistrationHandler;
import org.example.services.handlers.StartHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HandlerManager {
    private final static Map<String, IHandler> handlers = new HashMap<>();

    public HandlerManager(List<IHandler> handlersList) {
       handlers.put("/register", handlersList
               .stream()
               .filter(handler -> handler instanceof RegistrationHandler)
               .findFirst()
               .orElse(null));

        handlers.put("/login", handlersList
                .stream()
                .filter(handler -> handler instanceof LoginHandler)
                .findFirst()
                .orElse(null));

        handlers.put("/start", handlersList
                .stream()
                .filter(handler -> handler instanceof StartHandler)
                .findFirst()
                .orElse(null));
    }

    public IHandler getHandler(String command) {
        return handlers.get(command);
    }

}
