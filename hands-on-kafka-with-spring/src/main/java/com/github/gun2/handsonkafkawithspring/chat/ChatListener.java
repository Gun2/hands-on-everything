package com.github.gun2.handsonkafkawithspring.chat;

import com.github.gun2.handsonkafkawithspring.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatListener {
    private final ChatService chatService;

    @KafkaListener(topics = Topics.CHAT_TOPIC, groupId = "demo-group")
    public void listen(String message) {
        log.info("[consumer1]listen : {}", message);
        chatService.createChat(message, "consumer1");
    }

    @KafkaListener(topics = Topics.CHAT_TOPIC, groupId = "demo-group")
    public void listen2(String message) {
        log.info("[consumer2]listen : {}", message);
        chatService.createChat(message, "consumer2");
    }


}
