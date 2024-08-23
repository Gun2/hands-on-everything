package com.github.gun2.eurekaclient.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.TransportClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.cloud.netflix.eureka.http.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class EurekaClientConfig {

    private final EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier;

    @Bean
    public RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs = new RestTemplateDiscoveryClientOptionalArgs(
                this.eurekaClientHttpRequestFactorySupplier
        );
        restTemplateDiscoveryClientOptionalArgs.setSSLContext(SSLContextBuilder.create()
                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                .build());
        restTemplateDiscoveryClientOptionalArgs.setHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        return restTemplateDiscoveryClientOptionalArgs;
    }

    @Bean
    public RestTemplateTransportClientFactories restTemplateTransportClientFactories(RestTemplateDiscoveryClientOptionalArgs optionalArgs) {
        return new CustomRestTemplateTransportClientFactories(optionalArgs);
    }


    public class CustomRestTemplateTransportClientFactories extends RestTemplateTransportClientFactories {
        private final RestTemplateDiscoveryClientOptionalArgs args;
        public CustomRestTemplateTransportClientFactories(RestTemplateDiscoveryClientOptionalArgs args) {
            super(args);
            this.args = args;
        }

        @Override
        public TransportClientFactory newTransportClientFactory(final com.netflix.discovery.EurekaClientConfig clientConfig,
                                                                final Collection<Void> additionalFilters, final InstanceInfo myInstanceInfo,
                                                                final Optional<SSLContext> sslContext, final Optional<HostnameVerifier> hostnameVerifier) {
            return new CustomRestTemplateTransportClientFactory(this.args.getSSLContext(), this.args.getHostnameVerifier(), eurekaClientHttpRequestFactorySupplier);
        }
    }

    public class CustomRestTemplateTransportClientFactory extends RestTemplateTransportClientFactory {
        private final Optional<SSLContext> sslContext;
        private final Optional<HostnameVerifier> hostnameVerifier;

        public CustomRestTemplateTransportClientFactory(Optional<SSLContext> sslContext, Optional<HostnameVerifier> hostnameVerifier, EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier) {
            super(sslContext, hostnameVerifier, eurekaClientHttpRequestFactorySupplier);
            this.sslContext = sslContext;
            this.hostnameVerifier = hostnameVerifier;
        }

        @Override
        public EurekaHttpClient newClient(EurekaEndpoint serviceUrl) {
            return new RestTemplateEurekaHttpClient(restTemplate(serviceUrl.getServiceUrl()),
                    stripUserInfo(serviceUrl.getServiceUrl()));
        }


        private String stripUserInfo(String serviceUrl) {
            return UriComponentsBuilder.fromUriString(serviceUrl).userInfo(null).toUriString();
        }


        private RestTemplate restTemplate(String serviceUrl) {
            ClientHttpRequestFactory requestFactory = eurekaClientHttpRequestFactorySupplier
                    .get(this.sslContext.orElse(null), this.hostnameVerifier.orElse(null));
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            try {
                URI serviceURI = new URI(serviceUrl);
                if (serviceURI.getUserInfo() != null) {
                    String[] credentials = serviceURI.getUserInfo().split(":");
                    if (credentials.length == 2) {
                        restTemplate.getInterceptors()
                                .add(new BasicAuthenticationInterceptor(credentials[0], credentials[1]));
                    }
                }
            } catch (URISyntaxException ignore) {

            }

            restTemplate.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());
            restTemplate.setErrorHandler(new ErrorHandler());

            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("API_KEY", "API-KEY1");
                ClientHttpResponse response = execution.execute(request, body);
                if (!response.getStatusCode().equals(org.springframework.http.HttpStatus.NOT_FOUND)) {
                    return response;
                }
                return new NotFoundHttpResponse(response);
            });

            return restTemplate;
        }

        class ErrorHandler extends DefaultResponseErrorHandler {

            @Override
            protected boolean hasError(HttpStatusCode statusCode) {
                /**
                 * When the Eureka server restarts and a client tries to sent a heartbeat the
                 * server will respond with a 404. By default RestTemplate will throw an
                 * exception in this case. What we want is to return the 404 to the upstream
                 * code so it will send another registration request to the server.
                 */
                if (statusCode.is4xxClientError()) {
                    return false;
                }
                return super.hasError(statusCode);
            }
        }


        /**
         * Response that ignores body, specifically for 404 errors.
         */
        private static class NotFoundHttpResponse implements ClientHttpResponse {

            private final ClientHttpResponse response;

            NotFoundHttpResponse(ClientHttpResponse response) {
                this.response = response;
            }

            @Override
            public HttpStatusCode getStatusCode() throws IOException {
                return response.getStatusCode();
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return response.getRawStatusCode();
            }

            @Override
            public String getStatusText() throws IOException {
                return response.getStatusText();
            }

            @Override
            public void close() {
                response.close();
            }

            @Override
            public InputStream getBody() throws IOException {
                // ignore body on 404 for heartbeat, see gh-4145
                return null;
            }

            @Override
            public HttpHeaders getHeaders() {
                return response.getHeaders();
            }

        }

    }


}
