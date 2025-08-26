package com.github.gun2.handsonspringaiwithreference.chatmemory;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class JdbcChatMemory {

    @Autowired
    JdbcChatMemoryRepository chatMemoryRepository;
    @Autowired
    ChatModel chatModel;

    @Test
    void baseSample() {
        /** given */
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();

        /** when */
        // First interaction
        String conversationId = "007";
        UserMessage userMessage1 = new UserMessage("My name is James Bond");
        chatMemory.add(conversationId, userMessage1);
        ChatResponse response1 = chatModel.call(new Prompt(chatMemory.get(conversationId)));
        chatMemory.add(conversationId, response1.getResult().getOutput());

        // Second interaction
        UserMessage userMessage2 = new UserMessage("What is my name?");
        chatMemory.add(conversationId, userMessage2);
        ChatResponse response2 = chatModel.call(new Prompt(chatMemory.get(conversationId)));
        chatMemory.add(conversationId, response2.getResult().getOutput());

        /** then */
        String text = response2.getResult().getOutput().getText();
        System.out.println(text);
    }
}
