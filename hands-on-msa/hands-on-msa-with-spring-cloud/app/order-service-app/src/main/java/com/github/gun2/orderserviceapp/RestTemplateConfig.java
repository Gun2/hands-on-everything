package com.github.gun2.orderserviceapp;

import com.github.gun2.securitymodule.PassportUtil;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced  // Eureka를 통해 서비스 검색
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            String passport = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
            if (passport != null){
                request.getHeaders().add(PassportUtil.HEADER_NAME, passport);
            }
            return execution.execute(request, body);
        }));
        return restTemplate;
    }
}
