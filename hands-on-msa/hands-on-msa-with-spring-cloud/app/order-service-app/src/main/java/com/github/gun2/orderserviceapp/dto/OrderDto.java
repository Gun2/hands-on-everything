package com.github.gun2.orderserviceapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderDto {
    //주문 id
    private Long id;
    //상품 id
    private Long productId;
    //수량
    private Long amount;
    //전체 가격
    private Long totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    public static class CreateRequest {
        //상품 id
        @NotNull
        private Long productId;
        //수량
        @NotNull
        @Min(1)
        private Long amount;
    }

    @Getter
    public static class UpdateRequest extends CreateRequest{
        public UpdateRequest(Long productId, Long amount) {
            super(productId, amount);
        }
    }
}
