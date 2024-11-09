package org.example.configurations;

import org.example.services.HandlerManager;
import org.example.services.TelegramBot;
import org.example.services.UserSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.webhook-path}")
    private String webhookPath;

    @Value("${telegram.bot.api.key}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Bean
    public SetWebhook setWebhook() {
        return SetWebhook.builder()
                .url(webhookPath)
                .build();
    }

    @Bean
    public TelegramBot telegramBot(SetWebhook setWebhook,
                                   HandlerManager handlerManager,
                                   UserSessionManager userSessionManager) {
        TelegramBot telegramBot = new TelegramBot(setWebhook,
                botToken,
                botUsername,
                handlerManager,
                userSessionManager);

        try {
            telegramBot.execute(setWebhook);
            System.out.println("Webhook set: " + webhookPath);
        } catch (TelegramApiException e) {
            System.out.println("Webhook exception");
            throw new RuntimeException(e);
        }

        return telegramBot;
    }

}
