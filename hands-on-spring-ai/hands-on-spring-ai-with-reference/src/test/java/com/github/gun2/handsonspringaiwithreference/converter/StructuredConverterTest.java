package com.github.gun2.handsonspringaiwithreference.converter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class StructuredConverterTest {
    @Autowired
    private ChatModel chatModel;

    @JsonPropertyOrder({"actor", "movies"})
    record ActorsFilms(String actor, List<String> movies) { }

    @Autowired
    ChatClient.Builder  clientBuilder;

    ChatClient chatClient;
    @BeforeEach
    public void setup() {
        this.chatClient = clientBuilder.build();
    }

    @Test
    @DisplayName("chatClient를 사용한 Converter 사용법")
    void beanOutputConverterTest() {
        /** given */

        /** when */
        ActorsFilms actorsFilms = chatClient.prompt()
                .user(u -> u.text("Generate the filmography of 5 movies for {actor}.")
                        .param("actor", "Tom Hanks"))
                .call()
                .entity(ActorsFilms.class);

        /** then */
        System.out.println(actorsFilms);
    }

    @Test
    @DisplayName("ChatModel을 사용한 Converter 사용법")
    void beanOutputConverterLowLevelTest() {
        /** given */
        BeanOutputConverter<ActorsFilms> beanOutputConverter =
                new BeanOutputConverter<>(ActorsFilms.class);
        String format = beanOutputConverter.getFormat();
        String actor = "Tom Hanks";
        String template = """
        Generate the filmography of 5 movies for {actor}.
        {format}
        """;

        /** when */
        Generation generation = chatModel.call(
                PromptTemplate.builder().template(template).variables(Map.of("actor", actor, "format", format)).build().create()).getResult();

        String text = generation.getOutput().getText();
        ActorsFilms actorsFilms = beanOutputConverter.convert(text);

        /** then */
        System.out.println("format : " + format);
        System.out.println("actor : " + actor);
        System.out.println("template : " + template);
        System.out.println("text : " + text);
        System.out.println("actorsFilms : " + actorsFilms);

    }

    @Test
    void mapOutputConverterTest() {
        /** given */

        /** when */
        Map<String, Object> result = ChatClient.create(chatModel).prompt()
                .user(u -> u.text("Provide me a List of {subject}")
                        .param("subject", "an array of numbers from 1 to 9 under they key name 'numbers'"))
                .call()
                .entity(new ParameterizedTypeReference<Map<String, Object>>() {});

        /** then */
        System.out.println(result);
    }

    @Test
    void listOutputConverterTest() {
        /** given */

        /** when */
        List<String> flavors = ChatClient.create(chatModel).prompt()
                .user(u -> u.text("List five {subject}")
                        .param("subject", "ice cream flavors"))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));

        /** then */
        System.out.println(flavors);
    }
}