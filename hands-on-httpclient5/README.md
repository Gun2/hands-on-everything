# HttpClient5.3
HttpCore 기반으로 구현된 HTTP 에이전트로서 `client side 인증`, `상태관리`, `연결관리` 등을 재공하는 재사용가능한 컴포넌트

## Quick start
### 의존성 추가
```groovy
implementation 'org.apache.httpcomponents.client5:httpclient5:5.3.1'
```

### Get 요청 (동기)
```java
public void get(){
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
        ClassicHttpRequest httpGet = ClassicRequestBuilder.get("http://httpbin.org/get")
                .build();
        httpclient.execute(httpGet, response -> {
            System.out.println(response.getCode() + " " + response.getReasonPhrase());
            final HttpEntity entity1 = response.getEntity();
            EntityUtils.consume(entity1);
            return null;
        });
    }
}
```

### Post 요청 (동기)
```java
public void post(){
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
        ClassicHttpRequest httpPost = ClassicRequestBuilder.post("http://httpbin.org/post")
                .setEntity(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", "vip"),
                        new BasicNameValuePair("password", "secret"))))
                .build();
        httpclient.execute(httpPost, response -> {
            System.out.println(response.getCode() + " " + response.getReasonPhrase());
            final HttpEntity entity2 = response.getEntity();
            EntityUtils.consume(entity2);
            return null;
        });
    }
}
```

### Get 요청 (비동기)
```java
public void asyncGet(){
    try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
        httpclient.start();
        SimpleHttpRequest request1 = SimpleRequestBuilder.get("http://httpbin.org/get").build();
        Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
        //결과 반환
        SimpleHttpResponse response1 = future.get();
    }
}
```

### Post 요청 (비동기)
```java
public void asyncPost(){
    try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
        // Start the client
        httpclient.start();

        SimpleHttpRequest request1 = SimpleRequestBuilder.post("http://httpbin.org/post")
                .addParameters(
                        new BasicNameValuePair("username", "vip"),
                        new BasicNameValuePair("password", "secret")
                ).build();
        Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
        SimpleHttpResponse simpleHttpResponse = future.get();
    }
}
```


## 기능
- 표준 기반의 순수 Java로 구현된 HTTP 1.0, 1.1, 2.0(비동기 API만 지원) 지원
- HTTPS(SSL을 통한 HTTP) 프로토콜을 통한 암호화 지원
- 플러그형 소켓 팩토리 및 TLS 전략 지원
- HTTP/1.1 및 HTTP/1.0 프록시를 통한 투명한 메시지 교환
- CONNECT 메서드를 통해 HTTP/1.1 및 HTTP/1.0 프록시를 통한 터널링된 HTTPS 연결
- Basic, Digest, Bearer 인증 스킴 지원
- HTTP 상태 관리 및 쿠키 지원
- 유연한 연결 관리 및 풀링 지원
- HTTP 응답 캐싱 지원
- 소스 코드는 Apache 라이선스 하에 무료로 제공

## 기록 사항
### 연결 중 소비되지 않은 응답값의 재사용은 안전하지 않다.
> `NotFullyConsumeAndReuse.class` 코드 참고


# 참고링크
https://hc.apache.org