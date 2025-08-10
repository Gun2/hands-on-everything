# Product Service App
Product Service App은 상품 정보 조회/등록/수정/삭제와 같이 상품과 관련된 서비스를 제공하는 역할

# 구성 정보
`Spring Boot`를 통해 상품 관련 API를 제공하도록 구성됨

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
  port: 8084

spring:
  application:
    name: product-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

```

## API
| method | path          | 용도        |
|--------|---------------|-----------|
| GET    | products      | 상품 리스트 조회 |
| GET    | products/{id} | 상품 조회     |
| POST   | products      | 상품 생성     |
| PUT    | products/{id} | 상품 수정     |
| DELETE | products/{id} | 상품 삭제     |
