package com.github.gun2.handsonkafkawithspring.chat;

import com.github.gun2.handsonkafkawithspring.KafkaProducerService;
import com.github.gun2.handsonkafkawithspring.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final KafkaProducerService kafkaProducerService;

    public SendResult<String, String> sendChat(ChatRequest chatRequest) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<SendResult<String, String>> send = kafkaProducerService.send(Topics.CHAT_TOPIC, chatRequest.getMessage());
        return send.get(5, TimeUnit.SECONDS);
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
