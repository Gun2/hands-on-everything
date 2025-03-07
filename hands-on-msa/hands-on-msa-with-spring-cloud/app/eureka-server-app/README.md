# Eureka Server App
Eureka Server App은 서비스 레지스트리와 디스커버리 역할을 수행

# 구성 정보
`Spring Cloud Eureka`를 통해 마이크로서비스의 위치를 저장하고 라우팅 하도록 구성됨

## build.gradle
```groovy
ext {
	set('springCloudVersion', "2024.0.0")
}

dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
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
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

## Main Class (`EurekaServerApplication.java`)
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```