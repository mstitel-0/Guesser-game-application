package org.example.gameeservice.Services;

import jakarta.annotation.PostConstruct;
import org.example.gameeservice.DTOs.MessageDTO;
import org.example.gameeservice.DTOs.RiddleGenerationRequest;
import org.example.gameeservice.DTOs.RiddleGenerationResponse;
import org.example.gameeservice.Enums.GameTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Service
public class RiddleService {

    @Value("${gpt.model}")
    private String MODEL;

    private static String SYSTEM_INSTRUCTIONS;

    private static final String USER_ROLE = "user";

    private static final String SYSTEM_ROLE = "system";

    private final RestClient restClient;

    public RiddleService(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("riddle-system-instructions.txt");
        SYSTEM_INSTRUCTIONS = Files.readString(classPathResource.getFile().toPath());
    }

    public RiddleGenerationResponse getRiddle(GameTopic gameTopic) {
        MessageDTO userMessage = new MessageDTO(
                USER_ROLE,
                gameTopic.name()
        );
        MessageDTO systemMessage = new MessageDTO(
                SYSTEM_ROLE,
                SYSTEM_INSTRUCTIONS
        );

        RiddleGenerationRequest request = new RiddleGenerationRequest(
                MODEL,
                Arrays.asList(userMessage, systemMessage)
        );

        RiddleGenerationResponse response =
                restClient.post()
                        .body(request)
                        .retrieve()
                        .body(RiddleGenerationResponse.class);

        return response;
    }

    public String getHint(String riddle) {
        return null;
    }

}
