package com.github.gun2.websocketapp;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
class TimeController {

    private final SimpMessagingTemplate messagingTemplate;

    public TimeController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void sendTime() {
        String currentTime = LocalDateTime.now().toString();
        messagingTemplate.convertAndSend("/topic/time", currentTime);
    }

    @GetMapping("/current-time")
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
}
