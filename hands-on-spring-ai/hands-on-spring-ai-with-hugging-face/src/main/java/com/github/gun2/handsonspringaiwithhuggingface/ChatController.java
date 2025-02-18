package com.github.gun2.handsonspringaiwithhuggingface;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatDto.ChatResponse> chat(@RequestBody ChatDto.ChatRequest chatRequest) {
        ChatDto.ChatResponse chatResponse = chatbotService.chat(chatRequest);
        return ResponseEntity.ok(chatResponse);
    }
}
