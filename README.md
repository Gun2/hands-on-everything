# Hands On Spring Integration
spring integration 핸즈온 프로젝트

# 정리
## Spring Integration 구성 요소

### Message (메시지)
- Spring Integration의 핵심 데이터 단위.
- 메시지는 payload(실제 데이터)와 headers(메타데이터)로 구성.
- 메시지는 특정 채널을 통해 송수신.
```java
public interface Message<T> {
    T getPayload();
    MessageHeaders getHeaders();
}
```

### Channel (채널)
- 메시지를 주고받을 수 있는 파이프 또는 통로.
- Spring Integration에서는 다양한 유형의 채널을 지원.
- DirectChannel, QueueChannel, PublishSubscribeChannel 등이 있으며, 각 채널 유형에 따라 메시지 전달 방식이 다름.

### Bridge (브리지)
- 서로 다른 시스템, 프로토콜 또는 채널 간에 메시지를 전달하기 위한 구성 요소.
e.g. 서로 다른 채널 간의 데이터 흐름을 중계하거나 변환 가능.

### Service Activator (서비스 액티베이터)
- 메시지를 받아들이고, 처리하기 위한 구성 요소.
- 특정 비즈니스 로직이나 서비스를 실행하여 메시지를 처리.
- 일반적으로 POJO(Plain Old Java Object) 형태로 작성되며, Spring의 IoC(Inversion of Control)와 DI(Dependency Injection)를 통해 관리.

### Adapter (어댑터)
- 외부 시스템과 통합하기 위한 특수한 종류의 서비스 액티베이터.
- 외부 시스템의 프로토콜을 Spring Integration의 메시징 방식으로 변환. e.g. 파일 시스템, FTP, JMS, HTTP 등의 프로토콜을 지원하는 어댑터 존재.

### 의존성 추가
```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-integration'
    testImplementation 'org.springframework.integration:spring-integration-test'
    ...
}
```



# 참고
- https://spring.io/projects/spring-integration
- https://www.baeldung.com/spring-integration
