# Default MCP Client(MCP Client 예제)
기본 MCP Client starter(spring-ai-starter-mcp-client) 사용법에 대한 예제
> https://github.com/spring-projects/spring-ai-examples/tree/main/model-context-protocol/client-starter/starter-default-client

# 개요
해당 예제 코드에서는 Spring boot framework에서 pring AI MCP (Model Context Protocol) Client Boot Starter를 어떻게 사용하는지에 대한 샘플 코드를 담고있으며, 다양한 MCP 서버에 대한 연동을 수행한다.

## 앱이 하는것
- STDIO, SSE 전송방식을 통한 연결
- Spring AI의 Chat과 연동
- MCP Server를 통한 Tool 실행 시연
- `ai.user.input` 프로퍼티를 통해 사용자 질문 정의

## 사전 준비
- JDK 17이상
- Maven 3.6이상
- 엔트로픽 API Key
- Brave API Key

## 의존성
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-mcp-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-anthropic</artifactId>
    </dependency>
</dependencies>
```
## properties
### 공통
```properties
# Application Configuration
spring.application.name=mcp
spring.main.web-application-type=none

# AI Provider Configuration
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}

# Enable the MCP client tool-callback auto-configuration
spring.ai.mcp.client.toolcallback.enabled=true
```

### STDIO
```properties
spring.ai.mcp.client.stdio.connections.brave-search.command=npx
spring.ai.mcp.client.stdio.connections.brave-search.args=-y,@modelcontextprotocol/server-brave-search

# 또는 아래처럼 json 파일로 사용 가능
# spring.ai.mcp.client.stdio.servers-configuration=classpath:/mcp-servers-config.json

```

# 동작
이 애플리케이션은 명령라인과 기본적인 MCP 사용을 통해 Spring AI 통합을 시연하는데 목적을 두고 있다.
## 동작 과정
이 애플리케이션은 시작부터 종료까지 아래의 과정을 순차적으로 수행한다.
- 이 애플리케이션 시작 시 다양한 MCP client들을 설정한다.
- 설정한 MCP Tool과 함께 ChatClient를 빌드한다.
- 사전 정의된 질문을 모델에게 던진다.
- AI의 응답을 보여준다.
- 자동으로 애플리케이션을 종료한다.

# 실행하기
## 환경설정
앱 실행 시 필요한 환경 설정을 진행한다.
- ANTHROPIC_API_KEY : 앤트로픽 key
- BRAVE_API_KEY : brave api key
```properties
export ANTHROPIC_API_KEY=your-api-key

# For the Brave Search MCP server
export BRAVE_API_KEY=your-brave-api-key
```

## 빌드
애플리케이션을 실행할 수 있도록 빌드한다.
```shell
./mvnw clean install
```

## 애플리케이션 실행
애플리케이션을 실행한다.
```shell
# Run with the default question from application.properties
java -jar target/mcp-starter-default-client-0.0.1-SNAPSHOT.jar

# Or specify a custom question
java -Dai.user.input='Does Spring AI support MCP?' -jar target/mcp-starter-default-client-0.0.1-SNAPSHOT.jar
```
