package com.github.gun2.handsonspringaiwithhuggingface;

import jakarta.annotation.Nullable;

import java.util.UUID;

public class ChatDto {
    record ChatRequest(@Nullable UUID chatId, String question) {}

    record ChatResponse(UUID chatId, String answer) {}
}
