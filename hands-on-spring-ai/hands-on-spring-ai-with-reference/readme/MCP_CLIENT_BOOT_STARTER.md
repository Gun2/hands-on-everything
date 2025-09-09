# MCP Client boot starter
Spring AI MCP Client Boot starter는 MCP Client 기능들의 Auto configuration을 지원한다.

# 주요 지원 기능
- 다중 클라언트 인스턴스들 관리
- 자동 클라이언트 초기화
- 다양한 전송 방식 지원
- Spring AI의 tool과 연동
- 특정 tool의 필터링
- tool 이름 prefix 선언(충돌 방지)
- application 컨텍스트 종료 시 리소스 자동 정리
- MCP Client 생성 커스텀

# 스타터
## 기본 MCP Client
기본 starter는 STDIO, SSE, Streamable Http, Stateless Streamable Http 전송 방식으로 동시에 여러 서버와 연결을 할 수 있는 기능을 제공한다.

```groovy
// https://mvnrepository.com/artifact/org.springframework.ai/spring-ai-starter-mcp-client
implementation("org.springframework.ai:spring-ai-starter-mcp-client")
```

## WebFlux Client
기본 starter와 유사하지만 WebFlux기반의 Streamable-Http, Stateless Streamable-Http, SSE 전송을 지원한다.
> WebFlux기반의 `SSE, StreamableHttp`사용을 권장
```groovy
implementation("org.springframework.ai:spring-ai-starter-mcp-client-webflux")
```

# Properties 설정
## 공통
공통 properties는 `spring.ai.mcp.client`로 시작함

| 속성(Property)             | 설명(Description)                                      | 기본값(Default Value)   |
| ------------------------ | ---------------------------------------------------- | -------------------- |
| enabled                  | MCP 클라이언트 활성화/비활성화                                   | true                 |
| name                     | MCP 클라이언트 인스턴스 이름                                    | spring-ai-mcp-client |
| version                  | MCP 클라이언트 인스턴스 버전                                    | 1.0.0                |
| initialized              | 생성 시 클라이언트 초기화 여부                                    | true                 |
| request-timeout          | MCP 클라이언트 요청 타임아웃                                    | 20s                  |
| type                     | 클라이언트 유형(SYNC 또는 ASYNC). 모든 클라이언트는 동일 유형이어야 하며 혼합 불가 | SYNC                 |
| root-change-notification | 모든 클라이언트에 대한 루트 변경 알림 활성화/비활성화                       | true                 |
| toolcallback.enabled     | Spring AI 툴 실행 프레임워크와 MCP 툴 콜백 통합 활성화/비활성화           | true                 |

## MCP 어노테이션 Properties
MCP client 어노테이션 properties는 `spring.ai.mcp.client.annotation-scanner`로 시작함

| 속성(Property)             | 설명(Description)                                      | 기본값(Default Value)   |
| ------------------------ |------------------------------------------------------| -------------------- |
| enabled                  | MCP 어노테이션 scan 활성화                                   | true                 |

## STDIO 전송 properties
STDIO properties는 `spring.ai.mcp.client.stdio`로 시작함

| 속성(Property)                | 설명(Description)                  | 기본값(Default Value) |
| --------------------------- | -------------------------------- | ------------------ |
| servers-configuration       | JSON 형식으로 MCP 서버 구성 정보를 포함하는 리소스 | -                  |
| connections                 | 이름별 stdio 연결 구성 맵                | -                  |
| connections.\[name].command | MCP 서버 실행에 사용할 명령어               | -                  |
| connections.\[name].args    | 명령어에 전달할 인자 목록                   | -                  |
| connections.\[name].env     | 서버 프로세스에 적용할 환경 변수 맵             | -                  |

### Sample
```yml
spring:
  ai:
    mcp:
      client:
        stdio:
          root-change-notification: true
          connections:
            server1:
              command: /path/to/server
              args:
                - --port=8080
                - --mode=production
              env:
                API_KEY: your-api-key
                DEBUG: "true"
```

### Sample (Json file)
외부 JSON 파일을 사용해서 설정할 수 있음
```yaml
spring:
  ai:
    mcp:
      client:
        stdio:
          servers-configuration: classpath:mcp-servers.json
```
```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "/Users/username/Desktop",
        "/Users/username/Downloads"
      ]
    }
  }
}
```

## Streamable Http 전송
Streamable-HTTP와 Stateless Streamable-HTTP MCP servers 연결에 사용되며 `spring.ai.mcp.client.streamable-http`로 시작함

| 속성(Property)                 | 설명(Description)                        | 기본값(Default Value) |
| ---------------------------- | -------------------------------------- | ------------------ |
| connections                  | 이름별 Streamable HTTP 연결 구성 맵            | -                  |
| connections.\[name].url      | MCP 서버와 Streamable-HTTP 통신을 위한 기본 URL  | -                  |
| connections.\[name].endpoint | 연결에 사용할 Streamable-HTTP 엔드포인트(URL 접미사) | /mcp               |

### Sample
```yaml
spring:
  ai:
    mcp:
      client:
        streamable-http:
          connections:
            server1:
              url: http://localhost:8080
            server2:
              url: http://otherserver:8081
              endpoint: /custom-sse
```

## SSE 전송
SSE 전송 properties는 `spring.ai.mcp.client.sse`로 시작함

| 속성(Property)                     | 설명(Description)            | 기본값(Default Value) |
| -------------------------------- | -------------------------- | ------------------ |
| connections                      | 이름별 SSE 연결 구성 맵            | -                  |
| connections.\[name].url          | MCP 서버와 SSE 통신을 위한 기본 URL  | -                  |
| connections.\[name].sse-endpoint | 연결에 사용할 SSE 엔드포인트(URL 접미사) | /sse               |


### Sample
```yaml
spring:
  ai:
    mcp:
      client:
        sse:
          connections:
            server1:
              url: http://localhost:8080
            server2:
              url: http://otherserver:8081
              sse-endpoint: /custom-sse
```

# 기능
## 동기/비동기 유형
Starter는 동기/비동기 유형을 지원함
- 동기 : 기본값
- 비동기 : `spring.ai.mcp.client.type=ASYNC`설정으로 사용 가능

## MCP Client 커스터마이저
pring AI의 MCP 클라이언트는 콜백 인터페이스를 통해 다양한 동작을 맞춤 설정할 수 있다.
> 요청 시간 초과, 이벤트 처리, 메시지 처리 등 설정
> 
### 주요 커스터마이징 항목
- Request Configuration: 요청 시간 초과 설정
- Custom Sampling Handlers: 서버가 LLM 샘플링(completions, generations)을 요청할 때 클라이언트가 모델 접근, 선택, 권한을 제어
- File system (Roots) Access: 클라이언트가 서버에 접근 가능한 파일 시스템 경로(roots)를 제공하고 변경 시 알림
- Elicitation Handlers: 서버가 사용자에게 추가 정보를 요청할 수 있는 표준화된 처리
- Event Handlers: 특정 서버 이벤트 발생 시 클라이언트 알림
- 도구 목록 변경 알림
- 리소스 목록 변경 알림
- 프롬프트 목록 변경 알림
- Logging Handlers: 서버가 구조화된 로그 메시지를 클라이언트로 전송
- Progress Handlers: 서버가 구조화된 진행 상황 메시지를 클라이언트로 전송 
> 클라이언트는 최소 로그 레벨 설정으로 로그 세부 수준 제어 가능

#### 샘플 (Sync)
```java
@Component
public class CustomMcpSyncClientCustomizer implements McpSyncClientCustomizer {
    @Override
    public void customize(String serverConfigurationName, McpClient.SyncSpec spec) {

        // 요청 타임아웃 구성을 사용자 정의
        spec.requestTimeout(Duration.ofSeconds(30));

        // 클라이언트가 접근할 수 있는 루트 URI 설정
        spec.roots(roots);

        // 메시지 생성 요청 처리를 위한 커스텀 샘플링 핸들러 설정
        spec.sampling((CreateMessageRequest messageRequest) -> {
            // 샘플링 처리
            CreateMessageResult result = ...
            return result;
        });

        // 유도(Elicitation) 요청 처리를 위한 커스텀 핸들러 설정
        spec.elicitation((ElicitRequest request) -> {
            // 유도 처리
            return new ElicitResult(ElicitResult.Action.ACCEPT, Map.of("message", request.message()));
        });

        // 진행 상황 알림 수신 시 호출될 소비자 추가
        spec.progressConsumer((ProgressNotification progress) -> {
            // 진행 상황 알림 처리
        });

        // 사용 가능한 도구 목록 변경 시 호출될 소비자 추가
        // (도구 추가 또는 제거 등)
        spec.toolsChangeConsumer((List<McpSchema.Tool> tools) -> {
            // 도구 변경 처리
        });

        // 사용 가능한 리소스 목록 변경 시 호출될 소비자 추가
        // (리소스 추가 또는 제거 등)
        spec.resourcesChangeConsumer((List<McpSchema.Resource> resources) -> {
            // 리소스 변경 처리
        });

        // 사용 가능한 프롬프트 목록 변경 시 호출될 소비자 추가
        // (프롬프트 추가 또는 제거 등)
        spec.promptsChangeConsumer((List<McpSchema.Prompt> prompts) -> {
            // 프롬프트 변경 처리
        });

        // 서버로부터 로그 메시지 수신 시 호출될 소비자 추가
        spec.loggingConsumer((McpSchema.LoggingMessageNotification log) -> {
            // 로그 메시지 처리
        });
    }
}

```

#### 샘플 (Async)
```java
@Component
public class CustomMcpAsyncClientCustomizer implements McpAsyncClientCustomizer {
    @Override
    public void customize(String serverConfigurationName, McpClient.AsyncSpec spec) {
        // 요청 타임아웃 구성을 사용자 정의
        spec.requestTimeout(Duration.ofSeconds(30));
        ...
    }
}
```


## 전송 지원
auto configuration은 다양한 전송 유형을 지원함
- Stdio
- HttpClient기반 HTTP/SSE, StreamableHTTP
- WebFlux기반 HTTP/SSE, StreamableHTTP'

## Tool 필터링
탐색된 Tool을 `McpToolFilter`인터페이스로 필터링 할 수 있다.
```java
@Component
public class CustomMcpToolFilter implements McpToolFilter {

    @Override
    public boolean test(McpConnectionInfo connectionInfo, McpSchema.Tool tool) {
        // 연결 정보와 도구 속성에 기반한 필터링 로직
        // true 반환 시 도구 포함, false 반환 시 도구 제외

        // 예시: 특정 클라이언트에서 제공하는 도구는 제외
        if (connectionInfo.clientInfo().name().equals("restricted-client")) {
            return false;
        }

        // 예시: 이름이 "allowed_"로 시작하는 도구만 포함
        if (tool.name().startsWith("allowed_")) {
            return true;
        }

        // 예시: 도구 설명이나 다른 속성 기반으로 필터링
        if (tool.description() != null &&
            tool.description().contains("experimental")) {
            return false;
        }

        return true; // 기본적으로 나머지 도구는 모두 포함
    }
}

```
- clientCapabilities: MCP 클라이언트가 가지고 있는 기능 정보
- clientInfo: MCP 클라이언트의 이름과 버전 정보
- initializeResult: MCP 서버에서 초기화할 때 반환된 결과 데이터

## Tool 이름 Prefix
여러 MCP 서버에서 가져온 도구들을 통합할 때 이름 충돌을 방지하기 위해 도구 이름에 접두어(prefix)를 붙이는 기능 제공
### 구현 방식
McpToolNamePrefixGenerator 인터페이스를 통해 커스터마이징 가능.
별도의 구현체를 등록하지 않으면 McpToolNamePrefixGenerator.defaultGenerator()를 사용.

### 기본 동작 규칙
- 클라이언트 이름 축약 -> 언더스코어(_)로 구분된 단어의 첫 글자만 사용.
- Spring AI는 기본적으로 클라이언트 이름 뒤에 연결 이름(connection name)을 붙여줌.
- 서버 타이틀 추가 -> 서버 타이틀이 있으면 축약하지 않고 그대로 붙임.
- 기본적으로 타이틀은 connection name과 동일.
- 64자 제한 -> 길이가 64자를 넘으면 앞부분부터 잘라냄.

### 예시
- Client name: spring_ai_mcp_client_server1 
- Title: server1
- Tool: search
- tool 이름 : `s_a_m_c_s_server1_search`

```java
@Component
public class CustomToolNamePrefixGenerator implements McpToolNamePrefixGenerator {

    @Override
    public String prefixedToolName(McpConnectionInfo connectionInfo, Tool tool) {
        // 서버 이름과 버전 접두어 예시
        String serverName = connectionInfo.initializeResult().serverInfo().name();
        String serverVersion = connectionInfo.initializeResult().serverInfo().version();
        return serverName + "_v" + serverVersion.replace(".", "_") + "_" + tool.name();
    }
}
```

### 비활성화 방법
```java
@Configuration
public class McpConfiguration {

    @Bean
    public McpToolNamePrefixGenerator mcpToolNamePrefixGenerator() {
        return McpToolNamePrefixGenerator.noPrefix();
    }
}
```

## Tool Context -> MCP Meta 변환
Spring AI의 ToolContext를 MCP 툴 호출 시 전달할 메타데이터로 변환하는 기능 제공
> 예: 사용자 ID, 보안 토큰, 진행 상태 추적용 토큰 등
### 활용 예시
progressToken을 MCP Progress Flow로 전달해 장시간 실행 작업의 진행 상태를 추적 가능
```java
ChatClient.create(chatModel)
    .prompt("Tell me more about the customer with ID 42")
    .toolContext(Map.of("progressToken", "my-progress-token"))
    .call()
    .content();

```

### 기본 컨버터(ToolContextToMcpMetaConverter.defaultConverter())
- McpToolUtils.TOOL_CONTEXT_MCP_EXCHANGE_KEY 는 제외
- 값이 null 인 항목 제외
- 나머지는 그대로 메타데이터로 전달

### 커스텀 컨버터
```java
@Component
public class CustomToolContextToMcpMetaConverter implements ToolContextToMcpMetaConverter {

    @Override
    public Map<String, Object> convert(ToolContext toolContext) {
        if (toolContext == null || toolContext.getContext() == null) {
            return Map.of();
        }

        // ToolContext를 MCP 메타데이터로 변환하는 사용자 정의 로직
        Map<String, Object> metadata = new HashMap<>();

        // 예시: 모든 키에 사용자 정의 접두사(prefix) 추가
        for (Map.Entry<String, Object> entry : toolContext.getContext().entrySet()) {
            if (entry.getValue() != null) {
                metadata.put("app_" + entry.getKey(), entry.getValue());
            }
        }

        // 예시: 추가적인 메타데이터 삽입
        metadata.put("timestamp", System.currentTimeMillis());
        metadata.put("source", "spring-ai");

        return metadata;
    }
}

```

### 비활성화 (@Bean)
```java
@Configuration
public class McpConfiguration {

    @Bean
    public ToolContextToMcpMetaConverter toolContextToMcpMetaConverter() {
        return ToolContextToMcpMetaConverter.noOp();
    }
}
```
### 비활성화 (properties)
```properties
spring.ai.mcp.client.toolcallback.enabled=false
```

# MCP 클라이언트 어노테이션
MCP Client Boot Starter는 다양한 MCP 클라이언트 작업을 처리하기 위해 어노테이션이 붙은 메서드를 자동으로 감지하고 등록합니다:
- @McpLogging : MCP 서버에서 오는 로그 메시지 알림을 처리합니다.
- @McpSampling : MCP 서버에서 LLM 완료(Completions)를 위한 샘플링 요청을 처리합니다.
- @McpElicitation : 사용자로부터 추가 정보를 수집하기 위한 MCP 서버의 요청을 처리합니다.
- @McpProgress : 장시간 실행되는 작업의 진행 상황 알림을 처리합니다.
- @McpToolListChanged : 서버의 툴 목록이 변경될 때 알림을 처리합니다.
- @McpResourceListChanged : 서버의 리소스 목록이 변경될 때 알림을 처리합니다.
- @McpPromptListChanged : 서버의 프롬프트 목록이 변경될 때 알림을 처리합니다.

## Sample
```java
@Component
public class McpClientHandlers {

    // 서버(server1)에서 오는 로그 메시지를 처리
    @McpLogging(clients = "server1")
    public void handleLoggingMessage(LoggingMessageNotification notification) {
        System.out.println("로그 수신: " + notification.level() +
                          " - " + notification.data());
    }

    // 서버(server1)에서 오는 샘플링 요청 처리 (LLM 응답 생성)
    @McpSampling(clients = "server1")
    public CreateMessageResult handleSamplingRequest(CreateMessageRequest request) {
        // 요청을 처리하고 응답 생성
        String response = generateLLMResponse(request);

        return CreateMessageResult.builder()
            .role(Role.ASSISTANT)                     // 응답 역할을 Assistant로 지정
            .content(new TextContent(response))       // 텍스트 콘텐츠로 응답 생성
            .model("gpt-4")                           // 모델 이름 지정
            .build();
    }

    // 서버(server1)에서 오는 진행 상황 알림 처리
    @McpProgress(clients = "server1")
    public void handleProgressNotification(ProgressNotification notification) {
        double percentage = notification.progress() * 100;
        System.out.println(String.format("진행률: %.2f%% - %s",
            percentage, notification.message()));
    }

    // 서버(server1)의 툴 목록이 변경되었을 때 처리
    @McpToolListChanged(clients = "server1")
    public void handleToolListChanged(List<McpSchema.Tool> updatedTools) {
        System.out.println("툴 목록 갱신됨: 현재 사용 가능 툴 개수 = " 
                           + updatedTools.size());
        // 로컬 툴 레지스트리 업데이트
        toolRegistry.updateTools(updatedTools);
    }
}

```

## Sample (Async)
```java
@McpLogging(clients = "server1")
public void handleServer1Logs(LoggingMessageNotification notification) {
    // Handle logs from specific server
    logToFile("server1.log", notification);
}

@McpSampling(clients = "server1")
public Mono<CreateMessageResult> handleAsyncSampling(CreateMessageRequest request) {
    return Mono.fromCallable(() -> {
        String response = generateLLMResponse(request);
        return CreateMessageResult.builder()
            .role(Role.ASSISTANT)
            .content(new TextContent(response))
            .model("gpt-4")
            .build();
    }).subscribeOn(Schedulers.boundedElastic());
}
```