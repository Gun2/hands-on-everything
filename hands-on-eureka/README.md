# Hands On Spring Eureka
spring eureka 핸즈온 프로젝트

# 프로젝트 구성
```
├── app
│   ├── eureka-client <- eureka client
│   └── eureka-server <- eureka server (port:8761)
```

# 정리
## Eureka Client 구성 방법
### 의존성 추가
```groovy
ext {
    set('springCloudVersion', "2022.0.4")
}

dependencies {
    ...
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    ...
}
```
### properties
```properties
# 연결할 eureka server url 설정 ※ defaultZone은 Map<String, String>형태의 key로 사용되기에 카멜 케이스로 작성
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## Eureka Server 구성 방법
### 의존성 추가

```groovy
ext {
    set('springCloudVersion', "2022.0.4")
}

dependencies {
    ...
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
    ...
}
```

### properties
```properties
# eureka server 기본 포트
server.port=8761
# 자기 자신을 eureka 클라이언트로서 등록하지 않도록 설정
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

### MainApplication
```java
@SpringBootApplication
@EnableEurekaServer //Eureka Server  활성화
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}

```

[//]: # ( TODO: eureka api 사용 예시 추가)

# 참고
- https://cloud.spring.io/spring-cloud-netflix/reference/html/
