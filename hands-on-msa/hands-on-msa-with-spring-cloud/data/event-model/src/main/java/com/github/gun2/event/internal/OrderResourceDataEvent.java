package com.github.gun2.event.internal;

import com.github.gun2.orderservice.dto.OrderDto;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class OrderResourceDataEvent extends ResourceDataEvent<OrderDto> {

    public OrderResourceDataEvent(OrderDto data, EventType eventType) {
        super(data, eventType);
    }
}
