package com.github.gun2.authserviceclient;

import com.github.gun2.authservice.dto.PassportResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthServiceClient {
    Mono<ResponseEntity<PassportResponse>> createPassport(String accessToken);
}
