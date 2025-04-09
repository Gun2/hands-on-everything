package com.github.gun2.authserviceclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AuthServiceClientConfiguration {

    @Value("${app.services.auth.url}")
    private String authServiceUrl;

    @Bean
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "false")
    public WebClient.Builder regularWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public AuthServiceClient authServiceClient(
            WebClient.Builder builder
    ){
        return new AuthServiceClientV1(builder, authServiceUrl);
    }

}
