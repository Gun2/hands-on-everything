package com.github.gun2.orderserviceapp.controller;

import com.github.gun2.orderserviceapp.dto.OrderDto;
import com.github.gun2.orderserviceapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> findAll(
            @RequestParam(required = false) Long productId
    ) {
        List<OrderDto> dtoList = orderService.findAll(productId);
        return ResponseEntity.ok(dtoList);
    }


    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> find(
            @PathVariable(name = "id") Long id
    ) {
        OrderDto dto = orderService.findById(id);
        return ResponseEntity.ok(dto);
    }



    @PostMapping("/orders")
    public ResponseEntity<OrderDto> create(
            @Validated @RequestBody OrderDto.CreateRequest createRequest
    ) {
        OrderDto dto = orderService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(dto);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderDto> update(
            @Validated @RequestBody OrderDto.UpdateRequest updateRequest,
            @PathVariable(name = "id") Long id
    ) {
        OrderDto dto = orderService.update(updateRequest, id);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") Long id
    ) {
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }
}
