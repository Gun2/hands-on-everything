package com.github.gun2.apigatewayapp.config;

import com.github.gun2.apigatewayapp.AppProperties;
import com.github.gun2.authservice.dto.PassportResponse;
import com.github.gun2.authserviceclient.AuthServiceClient;
import com.github.gun2.securitymodule.PassportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
public class GatewayConfig {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final AppProperties appProperties;
    private final AuthServiceClient authServiceClient;
    private final RouteDefinitionLocator routeDefinitionLocator;
    @Value("${app.websocket.end-point}")
    private String websocketEndpoint;

    @Bean
    public GlobalFilter globalFilter(){
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            boolean isPermittedRequest = appProperties.getPermittedPaths().stream().anyMatch(path -> antPathMatcher.match(request.getPath().value(), path));
            if (isPermittedRequest){
                return chain.filter(exchange);
            }
            HttpCookie sessionid = request.getCookies().getFirst("SESSION");
            if (sessionid == null) {
                throw new IllegalArgumentException("is not exits session id");
            }
            //passport 조회
            Mono<ResponseEntity<PassportResponse>> passportResponseMono = authServiceClient.createPassport(sessionid.getValue());
            return passportResponseMono.flatMap(entity -> {
                if(entity.getStatusCode().is2xxSuccessful()){
                    ServerHttpRequest requestWithPassport;
                    if (isWebSocket(request.getPath().toString())){
                        //passport 쿼리 파라미터에 넣기
                        requestWithPassport = request.mutate().header("Sec-WebSocket-Protocol", entity.getBody().getPassport()).build();

                    }else {
                        //passport 헤더에 넣기
                        requestWithPassport = request.mutate().header(PassportUtil.HEADER_NAME, entity.getBody().getPassport()).build();
                    }

                    return chain.filter(exchange.mutate().request(requestWithPassport).build());
                }else{
                    throw new RuntimeException("passport create error");
                }
            });
        };
    }

    private boolean isWebSocket(String path) {
        return path.substring(0, this.websocketEndpoint.length()).equals(this.websocketEndpoint);
    }

    public ServerHttpRequest addQueryParam(ServerHttpRequest request, String paramName, String paramValue) {
        URI originalUri = request.getURI();

        URI newUri = UriComponentsBuilder.fromUri(originalUri)
                .queryParam(paramName, paramValue)
                .build(true)
                .toUri();

        return new ServerHttpRequestDecorator(request) {
            @Override
            public URI getURI() {
                return newUri;
            }
        };
    }
}
