package com.github.gun2.event.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GlobalExternalEvent extends AbstractExternalEvent {

    public GlobalExternalEvent(String sourceService, String payload, String eventType) {
        super(sourceService, Topics.GLOBAL, payload, eventType);
    }
}
