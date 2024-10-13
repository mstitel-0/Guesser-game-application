package org.example.gameeservice.Services;

import static org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.*;
import static org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat.*;

import org.example.gameeservice.DTOs.RiddleDTO;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Models.Hint;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiddleService {

    private static String riddleTemplate;

    private static String hintTemplate;

    private final OpenAiApi openAiApi;

    private final OpenAiChatOptions openAiChatOptions;

    private final OpenAiChatModel openAiChatModel;

    public RiddleService(OpenAiApi openAiApi,
                         OpenAiChatOptions openAiChatOptions,
                         @Value("classpath:riddle-generation-system-instructions.txt") Resource riddleResource,
                         @Value("classpath:hint-generation-system-instructions.txt") Resource hintResource) throws IOException {
        this.openAiApi = openAiApi;
        this.openAiChatOptions = openAiChatOptions;
        this.openAiChatModel = new OpenAiChatModel(openAiApi, openAiChatOptions);

        riddleTemplate = Files.readString(riddleResource.getFile().toPath());
        hintTemplate = Files.readString(hintResource.getFile().toPath());
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

    public String getHint(String riddle, List<Hint> hints) {
        return makeRequest(String.class, prepareHintText(riddle, hints));
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
