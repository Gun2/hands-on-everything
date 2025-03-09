package com.github.gun2.authserviceclient;

import com.github.gun2.authservice.dto.PassportResponse;
import com.github.gun2.securitymodule.AccessTokenHandleUtil;
import com.github.gun2.securitymodule.AccessTokenInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AuthServiceClientV1 implements AuthServiceClient {
    private final WebClient restClient;
    public AuthServiceClientV1(WebClient.Builder builder) {
        this.restClient = builder.baseUrl("lb://AUTH-SERVICE").build();
    }

    @Override
    public Mono<ResponseEntity<PassportResponse>> createPassport(String accessToken) {
        Mono<ResponseEntity<PassportResponse>> entity = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/passport").build())
                .header(AccessTokenInfo.HEADER_NAME, AccessTokenHandleUtil.createHeaderValueOfAccessToken(accessToken))
                .retrieve()
                .toEntity(PassportResponse.class);
        return entity;
    }
}
