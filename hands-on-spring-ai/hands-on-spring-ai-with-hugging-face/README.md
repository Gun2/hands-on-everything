# Hands on Spring ai with hugging face
Hugging Face 모델을 Spring AI 및 Ollama와 통합하여 대화형 AI 챗봇 API 개발 핸즈온 프로젝트
> 참고 : https://www.baeldung.com/spring-ai-ollama-hugging-face-models
# 준비
Ollama를 도커 컨테이너로 실행하기 위해 Docker 서비스가 실행중이어야함

# 설정
해당 핸즈온 프로젝트를 구성하기 위한 설정 방법
## gradle 설정
```groovy

dependencies {
    ...
    implementation 'org.springframework.ai:spring-ai-ollama-spring-boot-starter'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.ai:spring-ai-bom:${springAiVersion}"
    }
}
```

## Ollama로 deepseek-r1 모델 실행
Chat을 구현하기 위하여 LLM모델인 deepseek-r1를 Ollama로 실행
```shell
$ ollama pull llama3.2:latest
$ ollama ps
NAME    ID    SIZE    PROCESSOR    UNTIL 
$ llama run llama3.2
$ ollama ps
NAME               ID              SIZE      PROCESSOR    UNTIL              
llama3.2:latest    a80c4f17acd5    3.5 GB    100% CPU     4 minutes from now  
```


### properties
```properties
# 모델이 없을 때만 다운로드하도록 설정
spring.ai.ollama.init.pull-model-strategy=when_missing
# deepseek-r1 모델을 사용
spring.ai.ollama.chat.options.model=deepseek-r1
# ollama url 설정 (default : localhost:11434)
# spring.ai.ollama.base-url=localhost:11434

```

## Chat bot
### Chat bot bean 구성
```java
@Configuration
class ChatbotConfiguration {
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient
                .builder(chatModel)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }
}
```

### Chat service 추가
```java
public ChatResponse chat(ChatRequest chatRequest) {
    UUID chatId = Optional
      .ofNullable(chatRequest.chatId())
      .orElse(UUID.randomUUID());
    String answer = chatClient
      .prompt()
      .user(chatRequest.question())
      .advisors(advisorSpec ->
          advisorSpec
            .param("chat_memory_conversation_id", chatId))
      .call()
      .content();
    return new ChatResponse(chatId, answer);
}
```

### Api 추가
```java
@PostMapping("/chat")
public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
    ChatResponse chatResponse = chatbotService.chat(chatRequest);
    return ResponseEntity.ok(chatResponse);
}
```

### chat api 요청
```shell
$ curl -X POST "http://localhost:8080/chat" -H "Content-Type: application/json" -d '{"question":"Who wanted to kill Harry Potter?"}'
{"chatId":"28b9ce3e-ea05-4266-8978-59b89a195195","answer":"<think>\nOkay, so I need to figure out who wanted to kill Harry Potter. I remember from the books that there are several bad guys with dark magic, but I'm not exactly sure which one specifically went after Harry. Let me think... \n\nI know that Voldemort is the main antagonist. He's been building up his power for a long time, right? I think he was once a wizard like Harry, but he turned into a dark being because of some dark arts. Then he escaped from Azkaban and went to Hogwarts to kill Harry.\n\nWait, so who exactly did Voldemort fight in the final battle at the end? I remember they had this big showdown where Voldemort fought back with his Horcruxes. But there was someone else involved too—maybe another wizard who supported Voldemort?\n\nOh yeah! There's a guy named Lestrange or something like that. His last name is Stann, if I recall correctly. He was an evil twin of Harry and wanted to kill him as well. Voldemort had a plan where he could take over the Ministry and kill Harry, but Lestrange stood in his way.\n\nSo putting it all together: Voldemort wanted Harry dead because he planned to take control of the Ministry and finish him off. To stop him, Lestrange was there, using dark magic against Voldemort's Horcruxes. That led to the final battle where they both fought each other, with Harry saving the day in the end.\n\nI think that's right, but I should double-check the names. Voldemort's name is Voldemort, and his twin is Lestrange Stann. They were working together to eliminate Harry so Voldemort could take over.\n</think>\n\nThe individuals who wanted to kill Harry Potter are Voldemort (Lord Voldemort) and Lestrange Stann. Voldemort, a dark wizard with a tragic past, sought to destroy Harry through a plan involving the Horcruxes. To counter Voldemort's advance, his twin Lestrange Stann used dark magic against Voldemort's Horcruxes at the final battle. Together, they aimed to eliminate Harry and take control of the Ministry of Magic. Despite this, Voldemort was ultimately defeated by Harry Potter with the help of Dumbledore, leading to Voldemort's destruction."}
```