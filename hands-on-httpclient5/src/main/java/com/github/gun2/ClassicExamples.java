package com.github.gun2;

import org.apache.hc.client5.http.*;
import org.apache.hc.client5.http.auth.AuthExchange;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.cookie.*;
import org.apache.hc.client5.http.impl.auth.CredentialsProviderBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpResponseParser;
import org.apache.hc.core5.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.hc.core5.http.io.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicLineParser;
import org.apache.hc.core5.http.message.LineParser;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.CharArrayBuffer;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
public class ClassicExamples {

    public static void main(String[] args) {
        try {
            configuration();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void configuration() throws Exception {

        // Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        final HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

            @Override
            public HttpMessageParser<ClassicHttpResponse> create(final Http1Config h1Config) {
                final LineParser lineParser = new BasicLineParser() {

                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (final ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }

                };
                return new DefaultHttpResponseParser(lineParser, DefaultClassicHttpResponseFactory.INSTANCE, h1Config);
            }

        };
        final HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Create HTTP/1.1 protocol configuration
        final Http1Config h1Config = Http1Config.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        // Create connection configuration
        final CharCodingConfig connectionConfig = CharCodingConfig.custom()
                //잘못된 입력에 대한 동작 방식 설정
                //CodingErrorAction.IGNORE : 예외 없이 무시, CodingErrorAction.REPORT : 예외 발생, CodingErrorAction.REPLACE: 특정 문자로 변환
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                //매핑할 수 없는 입력(문자열을 특정 문자셋으로 인코딩할 때 그 문자셋에 포함되지 않는 문자)에 대한 동작 방식 설정
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(StandardCharsets.UTF_8)
                .build();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        final HttpConnectionFactory<ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                h1Config, connectionConfig, requestWriterFactory, responseParserFactory);

        // Client HTTP connection objects when fully initialized can be bound to
        // an arbitrary network socket. The process of network socket initialization,
        // its connection to a remote address and binding to a local one is controlled
        // by a connection socket factory.

        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        final SSLContext sslContext = SSLContexts.createSystemDefault();

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);

        // Use custom DNS resolver to override the system DNS resolution.
        final DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                //아래의 myhost 호스트를 127.0.0.1 ip로 반환
                if (host.equalsIgnoreCase("myhost")) {
                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
                } else {
                    return super.resolve(host);
                }
            }

        };

        // Create a connection manager with custom configuration.
        final PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionFactory(connFactory)
                .setDnsResolver(dnsResolver)
                //동시성 제어 정책 설정 (STRICT: 엄격한 순서로 연결을 할당,LAX: 덜 엄격하게 연결을 할당)
                //STRICT는 예측 가능한 성능을 유지할 때 유용하고, LAX는 성능이 중요할 때 유용)
                .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                //연결 풀의 재사용 정책을 설정 (LIFO: 가장 최근에 반환된 연결을 가장 먼저 다시 사용, FIFO: 반환된 순서대로 다시 사용)
                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                .build();

        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(SocketConfig.custom()
                .setTcpNoDelay(true)
                .build());
        // Validate connections after 10 sec of inactivity
        connManager.setDefaultConnectionConfig(ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(30))
                .setSocketTimeout(Timeout.ofSeconds(30))
                .setValidateAfterInactivity(TimeValue.ofSeconds(10))
                .setTimeToLive(TimeValue.ofHours(1))
                .build());

        // Use TLS v1.3 only
        connManager.setDefaultTlsConfig(TlsConfig.custom()
                .setHandshakeTimeout(Timeout.ofSeconds(30))
                .setSupportedProtocols(TLS.V_1_3)
                .build());

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);
        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);

        // Use custom cookie store if necessary.
        final CookieStore cookieStore = new BasicCookieStore();
        // Use custom credentials provider if necessary.
        final CredentialsProvider credentialsProvider = CredentialsProviderBuilder.create()
                .build();
        // Create global request configuration
        final RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(StandardCookieSpec.STRICT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.DIGEST))
                .setProxyPreferredAuthSchemes(Collections.singletonList(StandardAuthScheme.BASIC))
                .build();

        // Create an HttpClient with the given custom dependencies and configuration.
        final int proxyPort = 8080;
        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setProxy(new HttpHost("localhost", proxyPort))
                .setDefaultRequestConfig(defaultRequestConfig)
                .build()) {
            final HttpGet httpget = new HttpGet("http://httpbin.org/get");
            // Request configuration can be overridden at the request level.
            // They will take precedence over the one set at the client level.
            final RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                    .setConnectionRequestTimeout(Timeout.ofSeconds(5))
                    .build();
            httpget.setConfig(requestConfig);

            // Execution context can be customized locally.
            // Contextual attributes set the local context level will take
            // precedence over those set at the client level.
            final HttpClientContext context = ContextBuilder.create()
                    .useCookieStore(cookieStore)
                    .useCredentialsProvider(credentialsProvider)
                    .build();

            SimpleProxy simpleProxy = new SimpleProxy();
            simpleProxy.run(proxyPort);
            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());
            httpclient.execute(httpget, context, response -> {
                System.out.println("----------------------------------------");
                System.out.println(httpget + "->" + new StatusLine(response));
                EntityUtils.consume(response.getEntity());
                return null;
            });
            simpleProxy.stop();
            // Last executed request
            HttpRequest request = context.getRequest();
            System.out.println("request : " + request);
            // Execution route
            RouteInfo httpRoute = context.getHttpRoute();
            System.out.println("httpRoute : " + request);
            // Auth exchanges
            Map<HttpHost, AuthExchange> authExchanges = context.getAuthExchanges();
            System.out.println("authExchanges : " + request);
            // Cookie origin
            CookieOrigin cookieOrigin = context.getCookieOrigin();
            System.out.println("cookieOrigin : " + request);
            // Cookie spec used
            CookieSpec cookieSpec = context.getCookieSpec();
            System.out.println("cookieSpec : " + request);
            // User security token
            Object userToken = context.getUserToken();
            System.out.println("userToken : " + request);
        }
    }
}
