package com.github.gun2.event.internal;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ResourceDataEvent<T> extends DomainEvent{
    private T data;
    private EventType eventType;

    public ResourceDataEvent(T data, EventType eventType) {
        this.data = data;
        this.eventType = eventType;
    }
}
