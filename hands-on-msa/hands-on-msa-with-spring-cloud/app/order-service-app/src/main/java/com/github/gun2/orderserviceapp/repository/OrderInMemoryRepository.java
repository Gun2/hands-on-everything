package com.github.gun2.orderserviceapp.repository;

import com.github.gun2.orderservice.dto.OrderDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 메모리를 사용하는 주문 저장소
 */
@Repository
public class OrderInMemoryRepository implements OrderRepository {
    private final AtomicLong idGenerator =  new AtomicLong(1);
    private final ConcurrentHashMap<Long, OrderDto> store = new ConcurrentHashMap<>();


    @Override
    public OrderDto save(OrderDto orderDto) {
        boolean exists = orderDto.getId() != null && store.get(orderDto.getId()) != null;
        if (exists) {
            OrderDto order = OrderDto.builder()
                    .id(orderDto.getId())
                    .productId(orderDto.getProductId())
                    .amount(orderDto.getAmount())
                    .totalPrice(orderDto.getTotalPrice())
                    .updatedAt(LocalDateTime.now())
                    .build();
            store.put(order.getId(), order);
            return order;
        } else {
            OrderDto order = OrderDto.builder()
                    .id(idGenerator.getAndIncrement())
                    .productId(orderDto.getProductId())
                    .amount(orderDto.getAmount())
                    .totalPrice(orderDto.getTotalPrice())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            store.put(order.getId(), order);
            return order;
        }
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public List<OrderDto> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public OrderDto findById(Long id) {
        return store.get(id);
    }
}
