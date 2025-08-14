package com.github.gun2.event.external;

import java.time.Instant;

public interface ExternalEvent {
    String getEventId();
    String getTopic();
    String getPayload();
    String getEventType();
    String getSourceService();
    Instant getOccurredAt();
}
