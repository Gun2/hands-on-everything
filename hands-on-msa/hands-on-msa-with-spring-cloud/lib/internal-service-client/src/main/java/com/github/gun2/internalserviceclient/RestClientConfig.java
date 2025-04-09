package com.github.gun2.internalserviceclient;

import com.github.gun2.securitymodule.PassportUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Configuration
public class RestClientConfig {

    @Bean
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "false")
    public RestClient.Builder regularRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    RestClient restClient(RestClient.Builder builder){
        return builder.requestInterceptor(new PassportInterceptor()).build();
    }

    static class PassportInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            String passport = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
            if (passport != null) {
                request.getHeaders().add(PassportUtil.HEADER_NAME, passport);
            }
            return execution.execute(request, body);
        }
    }
}
