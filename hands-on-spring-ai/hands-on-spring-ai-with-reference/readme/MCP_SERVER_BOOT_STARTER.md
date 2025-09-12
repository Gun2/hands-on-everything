# MCP Server boot starter
Spring AI MCP Server Boot starter는 MCP Server 기능들의 Auto configuration을 지원한다.

## MCP Server
MCP Server란 Spring AI에게 표준화된 방식으로 특정 기능적인 부분들에 대한 제공 역량들을 노출하는 프로그램이다.


## 주요 지원 기능
- 자동 MCP 서버 컴포넌트 구성(Tool, 리소스, 프롬프트 등)
- STDIO, SSE, Streamable HTTP, Stateless Server의 다른 MCP 프로토콜 지원
- 동기/비동기 지원
- 다중 전송 계층 옵션
- 유연한 tool, 리소스, 프롬프트 스펙
- 변경 알림 기능 (Tool, 리소스 등 변경 시)
- 어노테이션 기반 개발 (자동 스캔, 등록)

## MCP Server Boot Starters
MCP 서버들은 다양한 프로토콜과 전송 메커니즘을 제공한다. 
> 적절한 starter와 올바른 `spring.ai.mcp.server.protocol` property를 설정해야한다.

| Server Type             | Dependency                           | Property                                    |
|-------------------------|--------------------------------------|---------------------------------------------|
| STDIO                   | spring-ai-starter-mcp-server         | `spring.ai.mcp.server.stdio=true`           |
| SSE WebMVC              | spring-ai-starter-mcp-server-webmvc  | `spring.ai.mcp.server.protocol=SSE` (또는 비움) |
| Streamable-HTTP WebMVC  | spring-ai-starter-mcp-server-webmvc  | `spring.ai.mcp.server.protocol=STREAMABLE`  |
| Stateless WebMVC        | spring-ai-starter-mcp-server-webmvc  | `spring.ai.mcp.server.protocol=STATELESS`   |
| SSE WebFlux             | spring-ai-starter-mcp-server-webflux | `spring.ai.mcp.server.protocol=SSE` (또는 비움) |
| Streamable-HTTP WebFlux | spring-ai-starter-mcp-server-webflux | `spring.ai.mcp.server.protocol=STREAMABLE`  |
| Stateless WebFlux       | spring-ai-starter-mcp-server-webflux | `spring.ai.mcp.server.protocol=STATELESS`   |

# Server Capabilities
전송 유형에 따라서 MCP 서버는 아래와 같이 디양한 제공역량을 가진다

| Capability            | 설명                                  |
|-----------------------|-------------------------------------|
| Tools                 | 서버가 LLM이 호출할 수 있는 툴을 노출             |
| Resources             | 클라이언트에 표준화된 리소스 제공                  |
| Prompts               | 표준화된 프롬프트 템플릿 제공                    |
| Utility / Completions | 프롬프트나 리소스 URI에 대해 자동완성 제안 기능 제공     |
| Utility / Logging     | 클라이언트로 구조화된 로그 메시지 전송               |
| Utility / Progress    | 긴 실행 작업의 진행 상황을 알리는 알림 메시지 지원 (선택적) |
| Utility / Ping        | 서버 상태를 보고하는 헬스 체크 기능 (선택적)          |


# Server Protocols
MCP Server는 아래의 프로토콜을 지원한다.

| Protocol        | 설명                                                                    | 설정 방법                                        |
|-----------------|-----------------------------------------------------------------------|----------------------------------------------|
| STDIO           | 호스트 애플리케이션 내부에서 실행되는 서버용 프로토콜. 표준 입력/출력을 통해 통신.                       | `spring.ai.mcp.server.stdio=true`            |
| SSE             | 실시간 업데이트를 위한 Server-Sent Events 프로토콜. 서버는 독립 프로세스로 다중 클라이언트 지원.       | `spring.ai.mcp.server.protocol=SSE` (또는 비워둠) |
| Streamable-HTTP | HTTP POST/GET 요청 기반의 독립 프로세스 서버. 다중 클라이언트 및 선택적 SSE 스트리밍 지원. SSE를 대체. | `spring.ai.mcp.server.protocol=STREAMABLE`   |
| Stateless       | 요청 간 세션 상태를 유지하지 않는 무상태 서버. 마이크로서비스/클라우드 네이티브 환경에 적합.                 | `spring.ai.mcp.server.protocol=STATELESS`    |

# Sync/Async Server API Options
MCP Server API는 동기/비동기 방식을 지원한다.

| Server Type              | 설명                                                            | 설정 방법                             |
| ------------------------ | ------------------------------------------------------------- | --------------------------------- |
| Synchronous (Sync)   | `McpSyncServer` 기반. 기본 서버 타입. 단순한 요청-응답 패턴에 적합.               | `spring.ai.mcp.server.type=SYNC`  |
| Asynchronous (Async) | `McpAsyncServer` 기반. 비동기/논블로킹 처리에 최적화. Project Reactor 지원 내장. | `spring.ai.mcp.server.type=ASYNC` |

# MCP Server Annotations
MCP Server Boot Starters는 수동 설정 대신 선언적으로 포괄적인 어노테이션기반 개발을 제공한다.

## 핵심 어노테이션
- @McpTool : 메서드를 MCP 도구로 표시. JSON 스키마를 자동 생성.
- @McpResource : URI 템플릿을 통해 리소스 접근 기능 제공.
- @McpPrompt : AI와의 상호작용용 프롬프트 메시지 생성.
- @McpComplete : 프롬프트 입력 시 자동 완성 기능 제공.

## 특수 파라미터
- McpMeta : MCP 요청에서 메타데이터 접근.
- @McpProgressToken : 장시간 실행되는 작업의 진행 상황 토큰 수신.
- McpSyncServerExchange / McpAsyncServerExchange : 동기/비동기 서버에서 고급 작업을 위한 전체 서버 컨텍스트 제공.
- McpTransportContext : 상태 비저장(stateless) 작업용 경량 컨텍스트 제공.
- CallToolRequest : 유연한 도구 지원을 위한 동적 스키마 제공.

### 샘플
```java
@Component
public class CalculatorTools {

    @McpTool(name = "add", description = "Add two numbers together")
    public int add(
            @McpToolParam(description = "First number", required = true) int a,
            @McpToolParam(description = "Second number", required = true) int b) {
        return a + b;
    }

    @McpResource(uri = "config://{key}", name = "Configuration")
    public String getConfig(String key) {
        return configData.get(key);
    }
}
```

### Auto configuration
Spring boot의 Auto configuration에 의해 자동으로 감지하고 등록됨
```java
@SpringBootApplication
public class McpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}
```
- MCP 어노테이션 스캔
- 적절한 스펙 생성
- MCP 서버와 함께 등록
- 설정에 의해 동기/비동기 구현 조절

### Configuration properties
server 어노테이션 스캐너 설정
```yaml
spring:
  ai:
    mcp:
      server:
        type: SYNC  # or ASYNC
        annotation-scanner:
          enabled: true
```