package com.github.gun2.orderserviceapp.service;

import com.github.gun2.event.internal.EventType;
import com.github.gun2.event.internal.OrderResourceDataEvent;
import com.github.gun2.externaleventrouter.EventRouter;
import com.github.gun2.orderservice.dto.OrderDto;
import com.github.gun2.orderserviceapp.client.PaymentRestClient;
import com.github.gun2.orderserviceapp.client.ProductRestClient;
import com.github.gun2.orderserviceapp.repository.OrderRepository;
import com.github.gun2.productservice.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRestClient productRestClient;
    private final PaymentRestClient paymentRestClient;
    private final EventRouter eventRouter;

    public OrderDto order(OrderDto.CreateRequest createRequest) {
        return order(createRequest, null);
    }
    public OrderDto order(OrderDto.CreateRequest createRequest, Long orderId) {
        ResponseEntity<ProductDto> productDtoResponseEntity = productRestClient.getProductById(createRequest.getProductId());
        if (productDtoResponseEntity.getStatusCode().isError()){
            throw new RuntimeException("상품 호출 실패");
        }
        ProductDto productDto = productDtoResponseEntity.getBody();
        return OrderDto.builder()
                .id(orderId)
                .productId(createRequest.getProductId())
                .amount(createRequest.getAmount())
                .totalPrice(productDto.getPrice() * createRequest.getAmount())
                .build();
    }

    @Transactional
    public OrderDto create(OrderDto.CreateRequest createRequest) {
        OrderDto order = order(createRequest);
        ResponseEntity<String> payResult = paymentRestClient.pay(order.getId());
        log.trace(payResult.getBody());
        OrderDto save = orderRepository.save(order);
        eventRouter.publish(new OrderResourceDataEvent(save, EventType.CREATED));
        return save;
    }

    public OrderDto update(OrderDto.UpdateRequest updateRequest, Long id) {
        OrderDto save = orderRepository.save(order(updateRequest, id));
        eventRouter.publish(new OrderResourceDataEvent(save, EventType.UPDATED));

        return save;
    }

    public void delete(Long id) {
        OrderDto orderDto = this.findById(id);
        orderRepository.delete(orderDto.getId());
        eventRouter.publish(new OrderResourceDataEvent(orderDto, EventType.DELETED));
    }

    public List<OrderDto> findAll(Long productId) {
        List<OrderDto> orderList = orderRepository.findAll();
        if (productId != null) {
            return orderList.stream().filter(
                    order -> order.getProductId().equals(productId)
            ).toList();
        }
        return orderList;
    }

    public OrderDto findById(Long id) {
        return orderRepository.findById(id);
    }


}
