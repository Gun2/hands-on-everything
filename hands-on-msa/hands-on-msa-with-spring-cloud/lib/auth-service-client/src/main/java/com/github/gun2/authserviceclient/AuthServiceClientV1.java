package com.github.gun2.authserviceclient;

import com.github.gun2.authservice.dto.PassportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AuthServiceClientV1 implements AuthServiceClient {
    private final WebClient restClient;
    public AuthServiceClientV1(WebClient.Builder builder, String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    @Override
    public Mono<ResponseEntity<PassportResponse>> createPassport(String sessionId) {
        Mono<ResponseEntity<PassportResponse>> entity = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/passport").build())
                .cookie("SESSION", sessionId)
                .retrieve()
                .toEntity(PassportResponse.class);
        return entity;
    }
}
