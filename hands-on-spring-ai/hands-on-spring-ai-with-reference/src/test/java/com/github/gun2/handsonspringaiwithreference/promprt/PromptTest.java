package com.github.gun2.handsonspringaiwithreference.promprt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class PromptTest {
    @Autowired
    ChatClient.Builder  clientBuilder;

    ChatClient chatClient;
    @BeforeEach
    public void setup() {
        this.chatClient = clientBuilder.build();
    }

    @Value("classpath:/prompts/sample_system_prompt_template.st")
    Resource promptTemplateResource;

    @Test
    void promptTemplateTest() {
        /** given */
        String adjective = "clever";
        String topic = "programmers";
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a {adjective} joke about {topic}");
        Prompt prompt = promptTemplate.create(Map.of("adjective", adjective, "topic", topic));

        /** when */
        String content = chatClient.prompt(prompt).call().content();

        /** then */
        System.out.println(content);
    }

    @Test
    void systemPromptTemplateTest() {
        /** given */
        String name = "Ari";
        String voice = "chill friend";
        String userText = """
            Tell me about three famous pirates from the Golden Age of Piracy and why they did.
            Write at least a sentence for each pirate.
            """;


        UserMessage userMessage = UserMessage.builder().text(userText).build();

        String systemText = """
          You are a helpful AI assistant that helps people find information.
          Your name is {name}
          You should reply to the user's request with your name and also in the style of a {voice}.
          """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        //resource를 직접 전달 가능
        //SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(promptTemplateResource);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        /** when */
        String content = chatClient.prompt(prompt).call().content();

        /** then */
        System.out.println(content);
    }
}
