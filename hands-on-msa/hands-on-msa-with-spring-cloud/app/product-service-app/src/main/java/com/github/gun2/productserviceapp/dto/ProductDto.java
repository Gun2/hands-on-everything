package com.github.gun2.productserviceapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private Long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    public static class CreateRequest {
        @NotBlank
        private String name;
        @NotNull
        @Min(0)
        private Long price;
    }

    @Getter
    public static class UpdateRequest extends CreateRequest {
        public UpdateRequest(String name, Long price) {
            super(name, price);
        }
    }
}
