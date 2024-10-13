package org.example.gameeservice.Configurations;

import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiApiConfiguration {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${openai.api.gpt.model}")
    private String model;

    @Value("${openai.api.gpt.max-completion-tokens}")
    private int maxCompletionTokens;

    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi(apiKey);
    }

    @Bean
    public OpenAiChatOptions openAiChatOptions() {
        return OpenAiChatOptions.builder()
                .withModel(model)
                .withMaxCompletionTokens(maxCompletionTokens)
                .build();
    }

}
