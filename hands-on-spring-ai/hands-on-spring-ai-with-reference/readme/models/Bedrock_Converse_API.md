# Bedrock Converse API
AWS에서 제공하는 서비스로서 function/tool calling, 멀티모달 입력, 스트리밍 응답 등 통합된 대화 인터페이스를 제공
> https://docs.spring.io/spring-ai/reference/1.1-SNAPSHOT/api/chat/bedrock-converse.html

# 주요 기능
- Function/tool calling: 함수 정의 및 대화 중 도구 호출 가능
- 멀티모달 입력: 텍스트 + 이미지, 동영상, 문서 등 입력 가능
- 스트리밍 응답: 실시간 응답 스트리밍 제공

# Prerequisites
> 상세 링크 : https://docs.aws.amazon.com/bedrock/latest/userguide/getting-started.html

## Dependency
```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-starter-model-bedrock-converse'
}
```

## Properties 
### Bedrock 연결 설정
| 속성                                    | 설명                        | 기본값         |
| ------------------------------------- | ------------------------- | ----------- |
| `spring.ai.bedrock.aws.region`        | 사용할 AWS 리전 설정             | `us-east-1` |
| `spring.ai.bedrock.aws.timeout`       | AWS 요청 타임아웃 설정            | `5m`        |
| `spring.ai.bedrock.aws.access-key`    | AWS Access Key            | 없음          |
| `spring.ai.bedrock.aws.secret-key`    | AWS Secret Key            | 없음          |
| `spring.ai.bedrock.aws.session-token` | 임시 자격증명을 위한 Session Token | 없음          |

### 모델 세부 설정
| 속성            | 설명                              | 기본값         |
| ------------- | ------------------------------- | ----------- |
| `model`       | 사용할 모델 ID (AWS Bedrock 콘솔에서 확인) | 없음          |
| `temperature` | 출력의 다양성 제어 (0.0 \~ 1.0)         | `0.8`       |
| `top-p`       | 누적 확률 기반 토큰 샘플링                 | Bedrock 기본값 |
| `top-k`       | 다음 토큰 선택 시 고려할 후보 개수            | Bedrock 기본값 |
| `max-tokens`  | 응답의 최대 토큰 수                     | `500`       |

### 샘플
```properties
spring.ai.model.chat=bedrock-converse
spring.ai.bedrock.aws.region=us-east-1
spring.ai.bedrock.aws.access-key=...
spring.ai.bedrock.converse.chat.options.model=anthropic.claude-3-5...
spring.ai.bedrock.converse.chat.options.temperature=0.8
```

# 런타임 옵션
기본 start up 옵션을 아래와 같이 덮어쓸 수 있음
```java
var options = BedrockChatOptions.builder()
        .model("anthropic.claude-3-5-sonnet-20240620-v1:0")
        .temperature(0.6)
        .maxTokens(300)
        .toolCallbacks(List.of(FunctionToolCallback.builder("getCurrentWeather", new WeatherService())
            .description("Get the weather in location. Return temperature in 36°F or 36°C format. Use multi-turn if needed.")
            .inputType(WeatherService.Request.class)
            .build()))
        .build();

String response = ChatClient.create(this.chatModel)
    .prompt("What is current weather in Amsterdam?")
    .options(options)
    .call()
    .content();
```

# Tool Calling
AI가 대화간 사용할 수 있는 Tool을 정의할 수 있음
```java
public class WeatherService {

    //@Tool 어노테이션으로 메서드 설명을 정의하고  @ToolParam어노테이션을 통하여 인자값 설명을 정의
    @Tool(description = "Get the weather in location")
    public String weatherByLocation(@ToolParam(description= "City or state name") String location) {
        ...
    }
}

String response = ChatClient.create(this.chatModel)
        .prompt("What's the weather like in Boston?")
        .tools(new WeatherService())
        .call()
        .content();

//Function 예시
@Bean
@Description("Get the weather in location. Return temperature in 36°F or 36°C format.")
public Function<Request, Response> weatherFunction() {
    return new MockWeatherService();
}

String response = ChatClient.create(this.chatModel)
        .prompt("What's the weather like in Boston?")
        .toolNames("weatherFunction")
        .inputType(Request.class)
        .call()
        .content();
```

# 멀티모달
모델이 텍스트뿐 아니라 이미지, 비디오, PDF, HTML 등 다양한 데이터 형식을 동시에 처리하고 이해하는 능력
> Amazon Bedrock Converse API는 멀티모달 입력을 지원하며, 이를 통해 복합적인 사용자 요청에 대해 정교한 텍스트 응답을 생성함

## 지원 형식
- 텍스트
- 이미지 (jpeg, png, gif, webp)
- 동영상 (mp4, webm, mov, flv, 등)
- 문서 (pdf, docx, xlsx, md, html, 등)

## Images
Bedrock은 Claude, Amazon Nova, Llama 3.2처럼 비전 지원 모델을 통해 이미지 기반 질문을 처리함
### 특징
- 여러 장의 이미지를 동시에 입력 가능
- 이미지 설명, 분류, 요약 등 다양한 시나리오에 활용
- Base64 인코딩 형식으로 이미지 전송 필요

### 예시 코드
```java
String response = ChatClient.create(chatModel)
    .prompt()
    .user(u -> u.text("Explain what do you see on this picture?")
        .media(Media.Format.IMAGE_PNG, new ClassPathResource("/test.png")))
    .call()
    .content();
```

## Video
Amazon Nova 모델은 단일 비디오 파일을 입력으로 받아 영상 기반 응답을 생성할 수 있음

### 특징
- Base64 또는 Amazon S3 URI를 통해 비디오 전송 가능
- 다양한 포맷 지원: mp4, webm, mov, flv, wmv, 등

### 예시 코드
```java
String response = ChatClient.create(chatModel)
    .prompt()
    .user(u -> u.text("Explain what do you see in this video?")
        .media(Media.Format.VIDEO_MP4, new ClassPathResource("/test.video.mp4")))
    .call()
    .content();
```

## Documents
Bedrock은 텍스트 기반 문서와 시각 기반 문서 모두를 입력받아 이해할 수 있음

### 유형
아래 2가지 유형으로 나뉨
- 텍스트 문서(txt, csv, html, md 등): 내용 기반 질문 응답
- 미디어 문서(pdf, docx, xlsx 등): 표, 그래프, 시각 자료 이해 및 요약

