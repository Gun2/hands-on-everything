package com.github.gun2.handsonkafkawithspring.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/chats")
    public List<Chat> list(){
        List<Chat> chatList = chatService.findAll();
        return chatList;
    }

    @PostMapping("/chats")
    public ResponseEntity<Boolean> create(
            @RequestBody ChatRequest chatRequest
    ){
        try {
            chatService.sendChat(chatRequest);
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            log.error("create chat exception : {0}", e);
            return ResponseEntity.internalServerError().body(false);
        }
    }

}
