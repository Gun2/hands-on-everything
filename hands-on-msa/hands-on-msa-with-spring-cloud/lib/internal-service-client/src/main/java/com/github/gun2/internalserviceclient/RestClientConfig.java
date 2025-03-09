package com.github.gun2.internalserviceclient;

import com.github.gun2.securitymodule.PassportUtil;
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
    @LoadBalanced
    RestClient restClient(RestClient.Builder builder){
        return builder.requestInterceptor(new PassportInterceptor()).build();
    }

    static class PassportInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            String passport = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
/*
            // SecurityContext 또는 RequestContext에서 passport 추출
            String passport = (String) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("passport", RequestAttributes.SCOPE_REQUEST);
*/

            if (passport != null) {
                request.getHeaders().add(PassportUtil.HEADER_NAME, passport);
            }
            return execution.execute(request, body);
        }
    }
}
