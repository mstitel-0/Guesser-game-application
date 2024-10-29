package org.example.gameeservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Models.Game;
import org.example.gameeservice.Models.Hint;
import org.example.gameeservice.Services.RiddleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class  RiddleServiceTest {
    @InjectMocks
    private RiddleService riddleService;

    @Mock
    private OpenAiApi openAiApi;

    @Mock
    private OpenAiChatOptions openAiChatOptions;

    @Mock
    private OpenAiChatModel openAiChatModel;
    @Mock
    private GameTopic gameTopic;

    @Mock
    private RestTemplate restTemplate;

    private static String RIDDLE_TEXT;

    private static String HINT_TEXT;

    private static final GameTopic ANIMAL_GAME_TOPIC = GameTopic.ANIMALS;
    private static final String ANIMAL_GAME_TOPIC_STRING = "ANIMALS";
    private static final String GENERATED_RIDDLE_TEXT = "RIDDLE TEXT";
    private static final String TOPIC_TO_REPLACE = "{topic}";
    private static final String RIDDLE_TO_REPLACE = "${riddle}";
    private static final String HINTS_TO_REPLACE = "${previous_hints}";
    private static final String HINT = "HINT TEXT";
    private static final Game GAME_INSTANCE = mock(Game.class);
    private static final Hint HINT_INSTANCE = new Hint(GAME_INSTANCE, HINT);
    private static final List<Hint> HINTS_LIST = List.of(HINT_INSTANCE);
    private static final String JSON_SCHEMA = "{\"some\": \"object\"}";

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        RIDDLE_TEXT = loadTextFromFile("riddle-generation-system-instructions.txt");
        HINT_TEXT = loadTextFromFile("hint-generation-system-instructions.txt");
        ReflectionTestUtils.setField(riddleService, "riddleTemplate", RIDDLE_TEXT);
        ReflectionTestUtils.setField(riddleService, "hintTemplate", HINT_TEXT);
    }

    private String loadTextFromFile(String fileName) throws IOException {
        Path path = Paths.get("src/main/resources/" + fileName);
        return Files.readString(path);
    }

    @Test
    public void prepareRiddleText(){
        String animalRiddleText = RIDDLE_TEXT.replace(TOPIC_TO_REPLACE, ANIMAL_GAME_TOPIC_STRING);
        when(gameTopic.name()).thenReturn(ANIMAL_GAME_TOPIC_STRING);

        String expectedText = riddleService.prepareRiddleText(ANIMAL_GAME_TOPIC);

        assertNotNull(expectedText);
        assertTrue(expectedText.contains(ANIMAL_GAME_TOPIC_STRING));
        assertEquals(expectedText, animalRiddleText);
    }

    @Test
    public void prepareHintText(){
        String hintText = HINT_TEXT.replace(RIDDLE_TO_REPLACE, GENERATED_RIDDLE_TEXT)
                .replace(HINTS_TO_REPLACE, HINTS_LIST.stream()
                        .map(Hint::getHint)
                        .collect(Collectors.joining()));

        String actualHintText = riddleService.prepareHintText(GENERATED_RIDDLE_TEXT, HINTS_LIST);

        assertNotNull(actualHintText);
        assertEquals(actualHintText, hintText);
    }

}
