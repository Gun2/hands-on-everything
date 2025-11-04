package com.github.gun2.chatapp.controller;

import com.github.gun2.chatapp.dto.ClientChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stream-chat-ai")
public class ChatAiController {

    private final ChatClient chatClient;

    public ChatAiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping
    public Flux<String> chat(@RequestBody ClientChatRequest chatRequest) {
        return this.chatClient.prompt(chatRequest.getContent()).stream().content();
    }
}
