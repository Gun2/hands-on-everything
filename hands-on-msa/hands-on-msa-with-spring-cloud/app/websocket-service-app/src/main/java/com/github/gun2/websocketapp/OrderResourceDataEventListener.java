package com.github.gun2.websocketapp;

import com.github.gun2.event.internal.OrderResourceDataEvent;
import com.github.gun2.event.internal.ResourceEvent;
import com.github.gun2.orderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderResourceDataEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void listen(OrderResourceDataEvent event) {
        OrderDto data = event.getData();
        if (true){
            throw new RuntimeException();
        }
        messagingTemplate.convertAndSend("/topic/products/" + data.getProductId() + "/orders", new ResourceEvent<>(data.getId(), event.getEventType()));
    }
}
