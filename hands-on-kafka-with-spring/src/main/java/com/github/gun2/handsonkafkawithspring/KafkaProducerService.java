package com.github.gun2.handsonkafkawithspring;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class KafkaProducerService {
    private final AtomicLong increment = new AtomicLong();
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, String>> send(String topic, String message) {
        return kafkaTemplate.send(topic, String.valueOf(increment.getAndIncrement() % 2),  message);
    }
}