package com.github.gun2.handsonkafkawithspring.chat;

import com.github.gun2.handsonkafkawithspring.KafkaProducerService;
import com.github.gun2.handsonkafkawithspring.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final KafkaProducerService kafkaProducerService;

    public void sendChat(ChatRequest chatRequest) {
        kafkaProducerService.send(Topics.CHAT_TOPIC, chatRequest.getMessage());
    }

    public List<Chat> findAll() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }


    public void createChat(String message, String consumerId) {
        synchronized (consumerId.intern()){
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
            );
        }
    }
}
