package com.github.gun2.eurekaclient.config;

import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class EurekaClientConfig {
    @Bean
    public RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs(
            EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier
    ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs = new RestTemplateDiscoveryClientOptionalArgs(
                eurekaClientHttpRequestFactorySupplier
        );

        restTemplateDiscoveryClientOptionalArgs.setSSLContext(SSLContextBuilder.create()
                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                .build());
        restTemplateDiscoveryClientOptionalArgs.setHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        return restTemplateDiscoveryClientOptionalArgs;
    }
}
