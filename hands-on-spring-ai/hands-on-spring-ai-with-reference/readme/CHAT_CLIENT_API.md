# Chat Client API
AI모델과 커뮤니케이팅 API를 제공하며 동기/비동기(스트리밍) 호출 지원
> https://docs.spring.io/spring-ai/reference/api/chatclient.html

# 사용 예시
Spring AI는 autoconfiguration을 지원하기 때문에 아래 샘플처럼 `ChatClient`를 inject받아 사용할 수 있음
```java
@RestController
class MyController {

    private final ChatClient chatClient;

    public MyController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai")
    String generation(String userInput) {
        return this.chatClient.prompt()
            .user(userInput)
            // ai 호출
            .call()
            // ai model의 응답 반환
            .content();
    }
}
```

# 특징
Chat Client API 특징
## 다중 모델 지원
Spring AI는 기본적으로 하나의 `ChatClient.Builder`만 autoconfiguration하지만 필요에 따라 여러 모델을 병렬로 사용할 수 있음

```properties
# 기본 설정 해제 후 직접 구현
spring.ai.chat.client.enabled=false
```
```java
@Bean
public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
    return ChatClient.create(chatModel);
}

@Bean
public ChatClient anthropicChatClient(AnthropicChatModel chatModel) {
    return ChatClient.create(chatModel);
}
```

## 프롬프트 템플릿 지원
{변수명} 형태의 템플릿 사용 가능
> 런타임에 .param("변수명", 값)으로 치환
```java
var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorsFilms>>() {});

Flux<String> flux = this.chatClient.prompt()
        .user(u -> u.text("""
                          Generate the filmography for a random actor.
                          {format}
                        """)
                .param("format", this.converter.getFormat()))
        .stream()
        .content();
```

## Advisor 기능
프롬프트 생성 전/후 처리에 사용되는 인터셉터 개념
- MessageChatMemoryAdvisor: 대화 히스토리 관리 
- QuestionAnswerAdvisor: 벡터 검색 기반 RAG 처리 
- SimpleLoggerAdvisor: 요청/응답 로깅
```java
ChatClient.builder(chatModel)
    .build()
    .prompt()
    .advisors(
        MessageChatMemoryAdvisor.builder(chatMemory).build(),
        QuestionAnswerAdvisor.builder(vectorStore).build()
    )
    .user(userText)
    .call()
    .content();
```