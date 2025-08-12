package com.github.gun2.orderserviceapp.client;

import com.github.gun2.productservice.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PaymentRestClient {

    @Value("${app.services.payment.url}")
    private String paymentUrl;
    private final RestClient restClient;

    public ResponseEntity<String> pay(Long orderId) {
        return restClient.get().uri(paymentUrl + "/payments/" + orderId)
                .retrieve()
                .toEntity(String.class);
    }
}
