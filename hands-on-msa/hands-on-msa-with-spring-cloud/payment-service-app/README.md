# Payment Service App
Payment Service App은 결제외 관련된 서비스를 제공하는 역할

# 구성 정보
`Spring Boot`를 통해 결제 관련 API를 제공하도록 구성됨
> Order Service에서 요청을 받을 수 있도록 `/payments/{orderId}` 엔드포인트를 제공.

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
  port: 8082

spring:
  application:
    name: payment-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

```

## Main Class (`PaymentServiceApplication.java`)

```java
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/payments")
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @GetMapping("/{orderId}")
    public String processPayment(@PathVariable String orderId) {
        return "Payment processed for Order ID: " + orderId;
    }
}

```