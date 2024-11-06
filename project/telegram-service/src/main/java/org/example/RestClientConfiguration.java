package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Value("${telegram.api.base-url}")
    private String TELEGRAM_BASE_URL;

    @Value("${telegram.bot.api.key}")
    private String TELEGRAM_API_KEY;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(TELEGRAM_BASE_URL + TELEGRAM_API_KEY)
                .build();
    }
}
