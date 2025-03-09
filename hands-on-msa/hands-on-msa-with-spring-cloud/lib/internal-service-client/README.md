# 모듈 설명
내부 서비스들 끼리 통신을 위해 사용되는 client 제공
> 서비스들의 인가를 위해 사용되는 passport 값이 자동으로 헤더에 추가됨

# 활용 방법
## 의존성 추가
```groovy
dependencies {
    ...
    implementation project(':lib:internal-service-client')
    ...
}
```
## 사용 예시
```java
@Controller
@RequiredArgsConstructor
public class OrderServiceAppApplication {
    private final RestClient restClient;
    
    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable String orderId) {
        //service 이름으로 호출 대상 서비스 명시
        String paymentResponse = restClient.get().uri("http://PAYMENT-SERVICE/payments/" + orderId).retrieve().body(String.class);//restTemplate.getForObject("http://PAYMENT-SERVICE/payments/" + orderId, String.class);
        return "Order ID: " + orderId + ", Payment: " + paymentResponse;
    }

}
```