package com.github.gun2.externaleventrouter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gun2.event.external.GlobalExternalEvent;
import com.github.gun2.event.internal.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventRouter {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventRouterProperties properties;
    private final String serviceName;

    public EventRouter(ObjectMapper objectMapper, ApplicationEventPublisher applicationEventPublisher, KafkaTemplate<String, Object> kafkaTemplate, EventRouterProperties properties, String serviceName) {
        this.objectMapper = objectMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
        this.serviceName = serviceName;
    }

    public <T extends DomainEvent> void publish(T event) {
        if (!properties.isEnabled()) return;
        applicationEventPublisher.publishEvent(event);
        GlobalExternalEvent externalEvent = null;
        try {
            externalEvent = new GlobalExternalEvent(serviceName, objectMapper.writeValueAsString(event), event.getClass().getTypeName());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(externalEvent.getTopic(), externalEvent);
    }
}