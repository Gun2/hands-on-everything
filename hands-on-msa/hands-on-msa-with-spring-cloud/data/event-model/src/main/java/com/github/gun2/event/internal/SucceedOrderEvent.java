package com.github.gun2.event.internal;

import com.github.gun2.orderservice.dto.OrderDto;
import lombok.Getter;

@Getter
public class SucceedOrderEvent extends DomainEvent {
    private final OrderDto order;

    public SucceedOrderEvent(OrderDto order) {
        this.order = order;
    }
}
