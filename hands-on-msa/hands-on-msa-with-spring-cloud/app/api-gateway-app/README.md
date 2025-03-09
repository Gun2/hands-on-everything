# Api Gateway App
Client로부터 요청을 받아 적절한 서비스로 라우팅을 시켜주는 역할

# 구성 정보
`Spring Cloud Gateway`와 `Spring Cloud Eureka`를 통해 마이크로서비스의 위치를 자동으로 감지하여 라우팅 하도록 구성됨

## build.gradle
```groovy

ext {
	set('springCloudVersion', "2024.0.0")
}

dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
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
  port: 8080

spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      discovery:
        locator:
          enabled: true  # Eureka에서 서비스 자동 검색 (Eureka에 등록된 서비스로 자동 라우팅 활성화)
      routes:
        - id: order-service
          uri: lb://ORDER-SERVICE
          predicates:
            - Path=/orders/**

        - id: payment-service
          uri: lb://PAYMENT-SERVICE
          predicates:
            - Path=/payments/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

```

## Main Class (`ApiGatewayApplication.java`)
```java
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

```

# Configuration

| Property        | YAML                        | Type     | Description          |
|-----------------|-----------------------------|----------|----------------------|
| Permitted Paths | app.gateway.permitted-paths | String[] | 인증 없이 라우팅 가능한 Path값들 |
