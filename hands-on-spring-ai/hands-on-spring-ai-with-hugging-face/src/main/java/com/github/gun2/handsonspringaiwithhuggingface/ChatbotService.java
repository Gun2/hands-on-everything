package com.github.gun2.handsonspringaiwithhuggingface;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatbotService {
    private final ChatClient chatClient;

    public ChatDto.ChatResponse chat(ChatDto.ChatRequest chatRequest) {
        UUID chatId = Optional
                .ofNullable(chatRequest.chatId())
                .orElse(UUID.randomUUID());
        String answer = chatClient
                .prompt()
                .user(chatRequest.question())
                .advisors(advisorSpec ->
                        advisorSpec
                                .param("chat_memory_conversation_id", chatId))
                .call()
                .content();
        return new ChatDto.ChatResponse(chatId, answer);
    }
}
