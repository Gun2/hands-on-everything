# MCP Annotations
Spring AI의 MCP 어노테이션들은 어노테이션 기반으로 MCP 서버 및 클라이언트를 다룰 수 있는 기능들을 제공한다. 
> https://docs.spring.io/spring-ai/reference/1.1-SNAPSHOT/api/mcp/mcp-annotations-overview.html
> 
## 특징
- 간단하게 MCP 서버와 클라이언트 핸들러들을 생성하고 등록할수 있음
- JAVA 어노테이션을 통해 클린하고 선언적인 방법 사용


# Architecture
MCP 어노테이션들은 아래 항목들로 구성되었다.

## Server Annotations
MCP 서버를 위해 다음의 어노테이션이 제공된다.
- `@McpTool` : 자동 JSON 스키마 생성과 함께 MCP Tool들을 구현
- `@McpResource` : URL 템플릿들을 통해 리소스 접근을 제공
- `@McpPrompt` : 프롬프트 메시지 생성
- `@McpComplete` : 자동 완성 기능 제공

## Client Annotations
MCP 클라이언트를 위해 다음의 어노테이션이 제공된다.
- `@McpLogging` : 로깅 메시지 알람들을 핸들링
- `@McpSampling` : 샘플링 요청 핸들링
- `@McpElicitation` : 추가 정보 수집을 위한 요청 도출 핸들링
- `@McpProgress` : 장기간 작업간 진행상태 알림 핸들링
- `@McpToolListChanged` : Tool 리스트 변경 알림 핸들링
- `@McpResourceListChanged` : 리소스 리스트 변경 알림 핸들링
- `@McpPromptListChanged` : 프롬프트 리스트 변경 알림 핸들링

## Special Parameters and Annotations
### McpSyncServerExchange
stateful한 동기 서버(로깅 알림, 진행상태 업데이트, 다른 서버측 동작 등)의 교환 기능을 제공하는 특수 파라미터이다.
이 파라미터는 JSON 스키마 생성으로부터 자동으로 주입되고 제외된다.

### McpAsyncServerExchange
stateful한 비동기 서버(reactive)의 교환 기능을 제공하는 특수 파라미터이다.
이 파라미터는 JSON 스키마 생성으로부터 자동으로 주입되고 제외된다.

### McpTransportContext
stateless한 동작(경량 접근)의 동작 기능 제공하는 특수 파라미터이다.
이 파라미터는 JSON 스키마 생성으로부터 자동으로 주입되고 제외된다.

### @McpProgressToken
요청으로부터 진행상태 토큰을 받기 위해 메서드 파라미터를 마킹한다.
이 파라미터는 JSON 스키마 생성으로부터 자동으로 주입되고 제외된다.

### McpMeta
MCP 요청, 알림, 결과로부터 메타데이터에 접근하기위한 특수 파라미터이다.
이 파라미터는 JSON 스키마 생성으로부터 자동으로 주입되고 제외된다.

# Getting Started
## 의존성 추가
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-mcp-annotations</artifactId>
</dependency>
```

### MCP Annotation을 포함하는 MCP Boot starter
- spring-ai-starter-mcp-client
- spring-ai-starter-mcp-client-webflux
- spring-ai-starter-mcp-server
- spring-ai-starter-mcp-server-webflux
- spring-ai-starter-mcp-server-webmvc

## 설정
### Client Annotation Scanner
```yaml
spring:
  ai:
    mcp:
      client:
        annotation-scanner:
          enabled: true  # 기본값 true
```
### Server Annotation Scanner
```yaml
spring:
  ai:
    mcp:
      server:
        annotation-scanner:
          enabled: true  # Enable/disable annotation scanning
```

# Quick Example
계산기 Tool 생성을 위한 MCP 어노테이션 예제
```java
@Component
public class CalculatorTools {

    @McpTool(name = "add", description = "Add two numbers together")
    public int add(
            @McpToolParam(description = "First number", required = true) int a,
            @McpToolParam(description = "Second number", required = true) int b) {
        return a + b;
    }

    @McpTool(name = "multiply", description = "Multiply two numbers")
    public double multiply(
            @McpToolParam(description = "First number", required = true) double x,
            @McpToolParam(description = "Second number", required = true) double y) {
        return x * y;
    }
}
```
## 로깅 예제
```java
@Component
public class LoggingHandler {

    @McpLogging(clients = "my-server")
    public void handleLoggingMessage(LoggingMessageNotification notification) {
        System.out.println("Received log: " + notification.level() +
                          " - " + notification.data());
    }
}
```