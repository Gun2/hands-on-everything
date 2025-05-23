package com.github.gun2.handsonkafkawithspring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatListener {
    private final ChatRepository chatRepository;

    @KafkaListener(topics = Topics.CHAT_TOPIC, groupId = "demo-group")
    public void listen(String message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        chatRepository.save(
                Chat.builder()
                        .message(message)
                        .createdAt(Instant.now())
                        .build()
        );    }

}
