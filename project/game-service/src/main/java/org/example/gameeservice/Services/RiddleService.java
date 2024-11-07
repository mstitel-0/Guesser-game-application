package org.example.gameeservice.Services;

import static org.springframework.ai.openai.api.ResponseFormat.*;

import jakarta.annotation.PostConstruct;
import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.DTOs.RiddleDTO;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Models.Hint;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiddleService {

    private String riddleTemplate;

    private String hintTemplate;

    private final OpenAiApi openAiApi;

    private final OpenAiChatOptions openAiChatOptions;

    private final OpenAiChatModel openAiChatModel;

    @Value("classpath:riddle-generation-system-instructions.txt")
    private Resource riddlePath;

    @Value("classpath:hint-generation-system-instructions.txt")
    private Resource hintPath;


    @PostConstruct
    public void init() throws IOException {
        this.riddleTemplate = new String(Files.readAllBytes(Paths.get(riddlePath.getURI())));
        this.hintTemplate = new String(Files.readAllBytes(Paths.get(hintPath.getURI())));
    }

    public RiddleService(OpenAiApi openAiApi, OpenAiChatOptions openAiChatOptions) throws IOException {
        this.openAiApi = openAiApi;
        this.openAiChatOptions = openAiChatOptions;
        this.openAiChatModel = new OpenAiChatModel(openAiApi, openAiChatOptions);
    }

    private <T> T makeRequest(Class<T> responseDTO, String promptText) {
        BeanOutputConverter<T> outputConverter = new BeanOutputConverter<>(responseDTO);
        openAiChatOptions.setResponseFormat(new ResponseFormat(Type.JSON_SCHEMA, outputConverter.getJsonSchema()));

        ChatResponse response = openAiChatModel.call(new Prompt(promptText));
        return outputConverter.convert(response.getResult().getOutput().getContent());
    }

    public RiddleDTO getRiddle(GameTopic gameTopic) {
        return makeRequest(RiddleDTO.class,  prepareRiddleText(gameTopic));
    }

    public HintDTO getHint(String riddle, List<Hint> hints) {
        return makeRequest(HintDTO.class, prepareHintText(riddle, hints));
    }

    public String prepareRiddleText(GameTopic gameTopic) {
        return riddleTemplate
                .replace("{topic}", gameTopic.name());
    }

    public String prepareHintText(String riddle, List<Hint> hints) {
        return hintTemplate
                .replace("${riddle}", riddle)
                .replace("${previous_hints}", hints.stream()
                        .map(Hint::getHint)
                        .collect(Collectors.joining()));
    }

}
