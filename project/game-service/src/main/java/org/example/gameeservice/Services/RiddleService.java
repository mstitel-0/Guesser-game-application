package org.example.gameeservice.Services;

import jakarta.annotation.PostConstruct;
import org.example.gameeservice.DTOs.OpenaiAPI.*;
import org.example.gameeservice.DTOs.RiddleDTO;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Models.Hint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiddleService {

    @Value("${gpt.model}")
    private String MODEL;

    private static String RIDDLE_GENERATION_SYSTEM_INSTRUCTIONS;

    private StringBuilder hintTemplate;

    private static final String USER_ROLE = "user";

    private static final String SYSTEM_ROLE = "system";

    private final RestClient restClient;

    public RiddleService(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        ClassPathResource riddleResource = new ClassPathResource("riddle-generation-system-instructions.txt");
        RIDDLE_GENERATION_SYSTEM_INSTRUCTIONS = Files.readString(riddleResource.getFile().toPath());

        ClassPathResource hintResource = new ClassPathResource("hint-generation-system-instructions.txt");
        hintTemplate = new StringBuilder(Files.readString(hintResource.getFile().toPath()));

    }

    public RiddleDTO getRiddle(GameTopic gameTopic) {
        OpenaiMessageDTO userMessage = new OpenaiMessageDTO(
                USER_ROLE,
                gameTopic.name()
        );
        OpenaiMessageDTO systemMessage = new OpenaiMessageDTO(
                SYSTEM_ROLE,
                RIDDLE_GENERATION_SYSTEM_INSTRUCTIONS
        );

        OpenaiAPIRequest request = new OpenaiAPIRequest(
                MODEL,
                Arrays.asList(userMessage, systemMessage)
        );



        RiddleRequest response = sendRiddleGenerationRequest(request);

        return new RiddleDTO(
                response.riddle(),
                response.answer()
        );
    }

    public String getHint(String riddle, List<Hint> hints) {
       prepareHintText(riddle, hints);

        OpenaiMessageDTO systemMessage = new OpenaiMessageDTO(
                SYSTEM_ROLE,
                hintTemplate.toString()
        );

        OpenaiAPIRequest request = new OpenaiAPIRequest(
                MODEL,
                Collections.singletonList(systemMessage)
        );

        HintGenerationResponse response = sendHintGenerationRequest(request);

        return response.hint();
    }

    public RiddleRequest sendRiddleGenerationRequest(OpenaiAPIRequest request) {
        return restClient.post()
                .body(request)
                .retrieve()
                .body(RiddleRequest.class);
    }

    public HintGenerationResponse sendHintGenerationRequest(OpenaiAPIRequest request) {
        return restClient.post()
                .body(request)
                .retrieve()
                .body(HintGenerationResponse.class);
    }

    public OpenaiMessageDTO extractMessage(RiddleGenerationResponse response) {
        return response.choices().getFirst().messages().getFirst();
    }

//    public String extractRiddle(RiddleGenerationResponse response) {
//        return extractMessage(response).content().split("\\.", 2)[0].trim();
//    }
//
//    public String extractAnswer(RiddleGenerationResponse response) {
//        return extractMessage(response).content().split("\\.", 2)[1].trim();
//    }

    public void prepareHintText(String riddle, List<Hint> hints) {
        String riddleToReplace = "${riddle}";
        String hintsToReplace = "${previous_hints}";

        int indexOfRiddle = hintTemplate.indexOf(riddleToReplace);
        hintTemplate.replace(indexOfRiddle,
                indexOfRiddle + riddleToReplace.length(),
                riddle
        );

        int indexOfHints = hintTemplate.indexOf(hintsToReplace);
        hintTemplate.replace(indexOfHints,
                indexOfHints + hintsToReplace.length(),
                hints.stream()
                        .map(Hint::getHint)
                        .collect(Collectors.joining())
        );
    }

}
