package com.github.gun2.handsonkafkawithspring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final KafkaProducerService kafkaProducerService;

    public void createChat(ChatRequest chatRequest) {
        kafkaProducerService.send(Topics.CHAT_TOPIC, chatRequest.getMessage());
    }

    public List<Chat> findAll() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }


}
