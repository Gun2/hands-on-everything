# 모듈 개요
app 서비스 모듈의 API 사용을 위한 클라이언트 유틸

# 활용 방법
## 의존성 추가
```groovy
dependencies {
    ...
	implementation project(':lib:auth-service-client')
    ...
}

```

## 샘플
```java

@RequiredArgsConstructor
@Component
public class Sample {
    private final AuthServiceClient authServiceClient;
    
    public void test(){
        Mono<ResponseEntity<PassportResponse>> passportResponseMono = authServiceClient.createPassport("access token");
    }
}

```