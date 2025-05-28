package com.github.gun2.handsonkafkawithspring.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public void create(
            @RequestBody ChatRequest chatRequest
    ){
        chatService.sendChat(chatRequest);
    }

}
