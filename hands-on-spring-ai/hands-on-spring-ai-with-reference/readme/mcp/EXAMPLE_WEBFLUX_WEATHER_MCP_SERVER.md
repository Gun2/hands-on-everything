# Spring MCP Server starter (webflux) 예제
해당 프로젝트는 `Spring AI MCP Server Boot Starter`의 webflux 전송방식을 가지고
National Weather Service API를 통해 날씨 정보를 노출하는 을 통해 MCP 서버를 구현하는 샘플이다.
> 샘플 : https://github.com/spring-projects/spring-ai-examples/tree/main/model-context-protocol/weather/starter-webflux-server

## 주요 항목
- `spring-ai-mcp-server-webflux-spring-boot-starter` 연게
- SSE와 STDIO 전송 둘 다 지원
- `@Tool`어노테이션을 통한 자동 tool 등록
- 날씨 관련 두 개의 tool(특정 위치 날씨 예측, US 날씨 알림)

# 의존성
## Spring AI MCP Server WebFlux Boot Starter
spring ai MCP server webflux 스타터는 아래의 기능을 제공함
- WebFlux를 사용한 반응형 전 (WebFluxSseServerTransport)
- 반응형 SSE 엔드포인트 Auto configuration
- 선택적 STDIO 전송 지원
- `spring-boot-starter-webflux`와 `mcp-spring-webflux` 의존성 포함

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-mcp-server-webflux-spring-boot-starter</artifactId>
</dependency>
```

# 빌드 방법
위의 샘플 프로젝트 설치 후 .../weather/starter-webflux-server 경로에서 아래의 명령어를 통해 빌드 수행
```shell
./mvnw clean install -Dmaven.test.skip=true

```

# 실행 방법
두 가지 전송 모드를 지원하며 아래는 전송 모드에 따른 실행 방법이다.
## WebFlux SSE Mode (Default)
```shell
java -jar target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar
```

## STDIO Mode
STDIO 전송 모드를 사용하기 위해서는 application.properties를 설정해야 한다.
```shell
java -Dspring.ai.mcp.server.stdio=true -Dspring.main.web-application-type=none -jar target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar
```

# 설정
application.properties를 통해 아래 정보들을 설정할 수 있다.
```properties
# 서버 식별 정보
spring.ai.mcp.server.name=my-weather-server
spring.ai.mcp.server.version=0.0.1

# 서버 유형 (SYNC/ASYNC)
spring.ai.mcp.server.type=SYNC

# 전송 설정
spring.ai.mcp.server.stdio=false
spring.ai.mcp.server.sse-message-endpoint=/mcp/message

# 알림 설정
spring.ai.mcp.server.resource-change-notification=true
spring.ai.mcp.server.tool-change-notification=true
spring.ai.mcp.server.prompt-change-notification=true

# 로깅 설정 (STDIO 전송에 필요함)
spring.main.banner-mode=off
logging.file.name=./target/starter-webflux-server.log
```

# Available Tools
## 날씨 예측 툴
- 이름 : getWeatherForecastByLocation
- 설명 : 특정 위도/경도의 날씨 예측 정보 가져오기
- 파라미터
   - 위도 : double
   - 경도 : double
- 예시
```java
CallToolResult forecastResult = client.callTool(new CallToolRequest("getWeatherForecastByLocation",
    Map.of("latitude", 47.6062, "longitude", -122.3321)));
```

## 날씨 알림 툴
- 이름 : getAlerts
- 설명 : US 주의 날씨 알림
- 파라미터
   - state : 주 코드(e.g., CA, NY)

# 서버 구현
자동 툴 등록을 위해 Spring boot와 Spring AI의 Tool 어노테이션을 사용함
```java
@SpringBootApplication
public class McpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    @Bean
    public List<ToolCallback> weatherTools(WeatherService weatherService) {
        return List.of(ToolCallbacks.from(weatherService));
    }
}
```

WeatherService는 `@Tool`어노테이션을 사용하여 날씨 tool을 구현함
```java
@Service
public class WeatherService {
    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        // Implementation using weather.gov API
    }

    @Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g., CA, NY)")
    public String getAlerts(String state) {
        // Implementation using weather.gov API
    }
}
```

# MCP Client
`STDIO`또는 `SSE` 전송방식을 사용하여 연결할 수 있음
## 샘플들
해당 샘플 프로젝트에는 아래 Client 연결 방식이 있음
- SampleClient.java: 수동 MCP Client 구현
- ClientStdio.java: STDIO 전송 연결
- ClientSse.java: SSE 전송 연결

## 수동 Client
### WebFlux SSE Client
SSE 전송방식을 사용하는 서버 연결을 위한 예시
```java
var transport = new WebFluxSseClientTransport(WebClient.builder().baseUrl("http://localhost:8080"));
var client = McpClient.sync(transport).build();
```

## Boot Starter Clients
날씨 MCP 서버와 연결하기 위하여 `starter-webflux-client`를 사용할 수 있음

### STDIO 전송 방식 서버, 클라이언트 실행 방법
1. 아래와 같이 `weather-mcp-servers-config.json` 생성
```json
{
  "mcpServers": {
    "weather-starter-webflux-server": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-Dspring.main.web-application-type=none",
        "-Dlogging.pattern.console=",
        "-jar",
        "/absolute/path/to/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```
2. 설정 파일을 사용하여 client 실행
> client는 샘플 프로젝트의 `starter-default-client` 사용
```shell
java -Dspring.ai.mcp.client.stdio.servers-configuration=file:weather-mcp-servers-config.json \
 -Dai.user.input='What is the weather in NY?' \
 -Dlogging.pattern.console= \
 -jar target/mcp-starter-default-client-0.0.1-SNAPSHOT.jar
```

### SSE 전송 방식 서버, 클라이언트 실행 방법
1. 서버 실행
```shell
java -jar target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar

```
2. 클라이언트 실행
```shell
java -Dspring.ai.mcp.client.sse.connections.weather-server.url=http://localhost:8080 \
 -Dlogging.pattern.console= \
 -Dai.user.input='What is the weather in NY?' \
 -jar target/mcp-starter-default-client-0.0.1-SNAPSHOT.jar
```