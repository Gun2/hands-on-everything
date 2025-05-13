# Websocket App
Websocket App은 웹 소켓을 통하여 현재 시간을 스트리밍 하는 역할

# 구성 정보
`Spring Boot Websocket`을 통해 실시간으로 현재 시간을 전달할 수 있는 구조를 가짐

## build.gradle
```groovy
ext {
    set('springCloudVersion', "2024.0.0")
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'org.springframework.boot:spring-boot-starter-websocket'
    implementation 'org.springframework.boot:spring-boot-starter-web'
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
  port: 8083

spring:
  application:
    name: websocket

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

```

## Configuration (`WebsocketConfig.java`)
websocket 설정 (STOMP, Sockjs 적용)
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
```

## Controller
현재시간 반환 API 및 현재시간 스트리밍 스케줄 구현
```java
@RestController
class TimeController {

    private final SimpMessagingTemplate messagingTemplate;

    public TimeController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void sendTime() {
        String currentTime = LocalDateTime.now().toString();
        messagingTemplate.convertAndSend("/topic/time", currentTime);
    }

    @GetMapping("/current-time")
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
}
```