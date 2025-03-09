package com.github.gun2.apigatewayapp.config;

import com.github.gun2.apigatewayapp.AppProperties;
import com.github.gun2.authservice.dto.PassportResponse;
import com.github.gun2.authserviceclient.AuthServiceClient;
import com.github.gun2.securitymodule.AccessTokenHandleUtil;
import com.github.gun2.securitymodule.AccessTokenInfo;
import com.github.gun2.securitymodule.PassportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
public class GatewayConfig {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final AppProperties appProperties;
    private final AuthServiceClient authServiceClient;

    @Bean
    public GlobalFilter globalFilter(){
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            boolean isPermittedRequest = appProperties.getPermittedPaths().stream().anyMatch(path -> antPathMatcher.match(request.getPath().value(), path));
            if (isPermittedRequest){
                return chain.filter(exchange);
            }
            String headerValue = request.getHeaders().getFirst(AccessTokenInfo.HEADER_NAME);
            if (headerValue == null) {
                throw new IllegalArgumentException("is not exits access token");
            }
            Optional<String> accessTokenOptional = AccessTokenHandleUtil.getAccessTokenFromHeaderValue(headerValue);
            if (accessTokenOptional.isEmpty()){
                throw new IllegalArgumentException("is not exits access token");
            }
            //passport 조회
            Mono<ResponseEntity<PassportResponse>> passportResponseMono = authServiceClient.createPassport(accessTokenOptional.get());
            return passportResponseMono.flatMap(entity -> {
                if(entity.getStatusCode().is2xxSuccessful()){
                    //passport 헤더에 넣기
                    ServerHttpRequest requestWithPassport = request.mutate().header(PassportUtil.HEADER_NAME, entity.getBody().getPassport()).build();
                    return chain.filter(exchange.mutate().request(requestWithPassport).build());
                }else{
                    throw new RuntimeException("passport create error");
                }
            });
        };
    }
}
