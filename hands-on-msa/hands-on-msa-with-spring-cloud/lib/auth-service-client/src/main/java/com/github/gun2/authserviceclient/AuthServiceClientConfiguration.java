package com.github.gun2.authserviceclient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AuthServiceClientConfiguration {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedRestClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public AuthServiceClient authServiceClient(
            WebClient.Builder builder
    ){
        return new AuthServiceClientV1(builder);
    }

}
