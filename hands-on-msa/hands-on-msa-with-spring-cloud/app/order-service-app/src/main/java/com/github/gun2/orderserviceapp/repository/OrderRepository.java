package com.github.gun2.orderserviceapp.repository;

import com.github.gun2.orderservice.dto.OrderDto;

import java.util.List;

public interface OrderRepository {
    OrderDto save(OrderDto orderDto);

    void delete(Long id);

    List<OrderDto> findAll();

    OrderDto findById(Long id);
}
