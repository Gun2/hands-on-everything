package com.github.gun2.handsonkafkawithspring.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    public List<KafkaContainerDto> findAll() {
        return kafkaListenerEndpointRegistry.getAllListenerContainers().stream().map(KafkaContainerDto::new).toList();
    }

    public KafkaContainerDto findById(String id) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        return new KafkaContainerDto(listenerContainer);
    }

    public KafkaContainerDto stop(String id) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        listenerContainer.stop();
        return new KafkaContainerDto(listenerContainer);
    }

    public KafkaContainerDto start(String id) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        listenerContainer.start();
        return new KafkaContainerDto(listenerContainer);    }
}
