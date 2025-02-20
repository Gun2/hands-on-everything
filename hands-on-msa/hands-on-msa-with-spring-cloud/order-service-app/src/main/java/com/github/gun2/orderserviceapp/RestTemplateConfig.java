package com.github.gun2.orderserviceapp;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced  // Eureka를 통해 서비스 검색
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
