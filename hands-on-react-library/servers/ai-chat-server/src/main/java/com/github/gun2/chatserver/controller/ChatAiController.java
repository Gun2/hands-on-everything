package com.github.gun2.chatserver.controller;

import com.github.gun2.chatserver.dto.ClientChatRequest;
import com.github.gun2.chatserver.dto.ClientChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-ai")
public class ChatAiController {

    private final ChatClient chatClient;

    public ChatAiController(ChatMemory chatMemory, ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }


    @PostMapping
    public ResponseEntity<ClientChatResponse> chat(@RequestBody ClientChatRequest chatRequest) {
        String content = this.chatClient.prompt(chatRequest.getContent()).call().content();
        ClientChatResponse clientChatResponse = ClientChatResponse.builder().answer(content).build();
        return ResponseEntity.ok(clientChatResponse);
    }
}
