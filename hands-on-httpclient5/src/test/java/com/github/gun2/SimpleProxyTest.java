package com.github.gun2;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleProxyTest {

    @Test
    void startAndStop() throws InterruptedException, IOException {
        /** given */
        SimpleProxy simpleProxy = new SimpleProxy();

        /** when */
        simpleProxy.run(8080);
        simpleProxy.stop();

        /** then */
        assertThat(simpleProxy.isRunning()).isFalse();
    }

    @Test
    void startAndGetRequestWithProxyAndStop() throws InterruptedException, IOException {
        /** given */
        SimpleProxy simpleProxy = new SimpleProxy();

        /** when */
        simpleProxy.run(8080);
        int statusCode;
        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setProxy(new HttpHost("localhost", 8080))
                .build()) {
            final HttpGet httpget = new HttpGet("http://httpbin.org/get");
            statusCode = httpclient.execute(httpget, response -> {
                System.out.println("----------------------------------------");
                System.out.println(httpget + "->" + new StatusLine(response));
                EntityUtils.consume(response.getEntity());
                return response.getCode();
            });
        }
        simpleProxy.stop();

        /** then */
        assertThat(simpleProxy.isRunning()).isFalse();
        assertThat(statusCode).isEqualTo(200);

    }
}