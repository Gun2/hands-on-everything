package com.github.gun2.orderserviceapp.client;

import com.github.gun2.productservice.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ProductRestClient {

    @Value("${app.services.product.url}")
    private String productUrl;
    private final RestClient restClient;

    public ResponseEntity<ProductDto> getProductById(Long productId) {
        return restClient.get().uri(productUrl + "/products/" + productId)
                .retrieve()
                .toEntity(ProductDto.class);
    }
}
