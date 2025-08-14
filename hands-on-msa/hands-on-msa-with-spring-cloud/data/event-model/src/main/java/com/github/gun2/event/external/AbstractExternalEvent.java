package com.github.gun2.event.external;

import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
public class AbstractExternalEvent implements ExternalEvent {
    private final String eventId = UUID.randomUUID().toString();
    private String sourceService;
    private String topic;
    private String payload;
    private String eventType;
    private final Instant occurredAt = Instant.now();

    public AbstractExternalEvent(String sourceService, String topic, String payload, String eventType) {
        this.sourceService = sourceService;
        this.topic = topic;
        this.payload = payload;
        this.eventType =  eventType;
    }

    @Override
    public String getEventId() {
        return this.eventId;
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public String getPayload() {
        return this.payload;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    @Override
    public String getSourceService() {
        return this.sourceService;
    }

    @Override
    public Instant getOccurredAt() {
        return this.occurredAt;
    }

}
