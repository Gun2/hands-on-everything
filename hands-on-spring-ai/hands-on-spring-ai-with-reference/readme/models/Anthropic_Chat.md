# Anthropic Chat
다양한 애플리케이션에서 사용되는 기본적인 모델로서 사용된다. (`Amazon Bedrock Converse`에서도 사용 가능함 )
> https://docs.spring.io/spring-ai/reference/1.1-SNAPSHOT/api/chat/anthropic-chat.html

# 특징
- 추론 기능 사용 가능
- 추론 기능에서 교차 추론 기능 제공 (spring ai에서는 미지원)
- 멀티모달 (이미지, PDF) 지원
- Tool Calling 지원

# Thinking (추론)
앤트로픽 클로드(Anthropic Claude)는 `thinking`기능을 제공한다.
> `thinking`기능은 답변에 도달하기까지의 추론을 보여주는 기능을 제공하는으로 아래 버전들이 제공함
> - Claude 4 models (claude-opus-4-20250514, claude-sonnet-4-20250514)
> - Claude 3.7 Sonnet (claude-3-7-sonnet-20250219)

## 추론 설정
추론을 사용하기 위해 요청값에 아래와 같은 설정을 추가할 수 있음
### 추론 옵션 추가하기
- `"type" : "enabled"` : 파라미터 추가
- `budget_tokens` : 추론에 할당하는 최대 토큰 수 제한

### Budget token 규칙
- `budget_tokens`은 max_tokens보다 작아야함
- 클로드는 할당된 토큰보다 적게 사용함 (할당된 budget_tokens 전부를 꼭 다 쓰지 않음)
- `budget_tokens`이 클수록 더 깊은 추론이 가능하지만 처리 시간이 증가함
- 추론간 tool 호출이 사용될 경우 `budget_tokens`를 유연하게 사용함 (`교차 추론`)
  > 아직 Spring ai에서 미지원하는 기능

### 핵심 고려사항들
- Claude 3.7은 전체 추론 과정을 응답에 포함하여 반환함
- Claude 4는 지연 시간을 줄이고 민감한 내용을 보호하기 위해 사고 과정의 요약본을 반환함
- 추론 과정에 사용된 토큰도 출력 토큰으로 청구됨. (응답에 모두 보이지 않아도 포함됨)
- `교차 추론` 기능은 Claude 4 모델에서만 사용 가능하며, 활성화를 위해 베타 헤더 interleaved-thinking-2025-05-14가 필요함

### 예시
```java
ChatResponse response = chatClient.prompt()
    .options(AnthropicChatOptions.builder()
        .model("claude-3-7-sonnet-latest")
        .temperature(1.0)  // Temperature should be set to 1 when thinking is enabled
        .maxTokens(8192)
        .thinking(AnthropicApi.ThinkingType.ENABLED, 2048)  // Must be ≥1024 && < max_tokens
        .build())
    .user("Are there an infinite number of prime numbers such that n mod 4 == 3?")
    .call()
    .chatResponse();

// For Claude 4 models - thinking is enabled by default
ChatResponse response4 = chatClient.prompt()
    .options(AnthropicChatOptions.builder()
        .model("claude-opus-4-0")
        .maxTokens(8192)
        // No explicit thinking configuration needed
        .build())
    .user("Are there an infinite number of prime numbers such that n mod 4 == 3?")
    .call()
    .chatResponse();
```

## 도구 사용
Claude 4 모델은 추론과 도구 사용을 통합하여 사용할 수 있음 (`교차 추론`)

## 추론 기능의 장점
- 투명성: 모델이 어떻게 결론에 도달했는지 이유 과정을 볼 수 있음.
- 디버깅: 논리적 오류가 발생한 부분을 찾을 수 있음.
- 교육: 단계별 사고 과정을 교육 도구로 활용 가능.
- 복잡한 문제 해결: 수학, 논리, 추론 문제에서 더 나은 결과를 낼 수 있음.

