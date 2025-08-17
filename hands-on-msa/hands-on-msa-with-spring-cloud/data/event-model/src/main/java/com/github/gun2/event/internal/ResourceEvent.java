package com.github.gun2.event.internal;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ResourceEvent<T> extends DomainEvent {
    private T id;
    private EventType eventType;

    public ResourceEvent(T id, EventType eventType) {
        this.eventType = eventType;
        this.id = id;
    }
}
