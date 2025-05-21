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
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(passportHandshakeInterceptor)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    ...
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

# 인증
인증된 사용자와의 websocket 연결을 위해 다음과 같은 과정을 수행
![webscoket_authentication.jpg](readme%2Fwebscoket_authentication.jpg)

## 핸드쉐이크 인증 (PassportHandshakeInterceptor.class)
웹소켓 연결 시 핸드쉐이크 과정에서 sec-websocket-protocol 헤더의 passport값을 통해 인증을 수행하고 정상적으로 인증되었으면 Establish 진행
```java
@Component
@RequiredArgsConstructor
public class PassportHandshakeInterceptor implements HandshakeInterceptor {
    private final PassportUtil passportUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //sec-websocket-protocol 헤더의 passport값을 통해 인증 수행
        String passport = request.getHeaders().getFirst("sec-websocket-protocol");
        if (passport != null && !passportUtil.isTokenExpired(passport)) {
            String role = passportUtil.extract(passport, ClaimNames.ROLE);
            String username = passportUtil.extractUsername(passport);
            //인증 객체를 attribute에 저장하여 추 후 ChannelInterceptor에서 인증에 활용
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, passport, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
            attributes.put("authentication",authentication);
            return true;
        }
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    private final PassportHandshakeInterceptor passportHandshakeInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                //인터셉터에 추가
                .addInterceptors(passportHandshakeInterceptor)
                .withSockJS();
    }
    ...
}

```

## Subscribe 인가
인증된 값 중 특정 역할을 가진 사용자만 연결만 구독될 수 있도록 아래 인터셉터 추가
```java
@Component
@Slf4j
public class PassportChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            Authentication authentication = (Authentication) accessor.getSessionAttributes().get("authentication");

            if (authentication != null && authentication.isAuthenticated()) {
                String userRole = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse("");


                if (destination.startsWith("/topic/time") && !userRole.equals("ROLE_ADMIN")) {
                    log.error("destination", destination);
                    throw new IllegalArgumentException("Access Denied: Admin Role Required");
                }
            }
        }
        return message;
    }
}


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    private final PassportChannelInterceptor passportChannelInterceptor;
    ...
    // subscribe를 인터셉터 하기 위해 inbound에 추가
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(passportChannelInterceptor);
    }
}

```