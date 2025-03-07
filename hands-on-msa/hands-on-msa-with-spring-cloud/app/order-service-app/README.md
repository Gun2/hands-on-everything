# Order Service App
Order Service App은 주문과 관련된 서비스를 제공하는 역할

# 구성 정보
`Spring Boot`를 통해 주문 관련 API를 제공하도록 구성됨

## build.gradle
```groovy
ext {
    set('springCloudVersion', "2024.0.0")
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    ...
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

## application.yml
```yaml
server:
  port: 8081

spring:
  application:
    name: order-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

```

## Main Class (`OrderServiceApplication.java`)
```java
@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced  // Eureka를 통해 서비스 검색
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderServiceAppApplication {
    private final RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceAppApplication.class, args);
    }
    
    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable String orderId) {
        //LoadBalanced restTemplate으로 인해 PAYMENT-SERVICE 서비스 address 조회 가능
        String paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payments/" + orderId, String.class);
        return "Order ID: " + orderId + ", Payment: " + paymentResponse;
    }
}
```