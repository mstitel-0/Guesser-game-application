package org.example.gameeservice.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfiguration {

    @Value("${gpt.api.key}")
    private String API_KEY;

    @Value("${gpt.api.base-url}")
    private String BASE_URL;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Authorization",  "Bearer " + API_KEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}
