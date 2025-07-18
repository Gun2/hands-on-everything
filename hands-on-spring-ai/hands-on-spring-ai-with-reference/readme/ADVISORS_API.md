# Advisors API
Advisors API는 LLM 요청을 가로채서 인터셉트, 데이터 가공 등의 기능들을 제공하기 때문에 더 정교하고 재사용성과 유지보수성 높은 AI 컴포넌트 제작이 가능하도록 도와줌
> https://docs.spring.io/spring-ai/reference/api/advisors.html

# 핵심 컴포넌트
![advisors_core_component.png](images/advisors_core_component.png)
## CallAroundAdvisor / CallAroundAdvisorChain
비동기 아닌(non-streaming) 시나리오에서 사용됨.

## StreamAroundAdvisor / StreamAroundAdvisorChain
Streaming 시나리오에서 사용됨

## AdvisedRequest / AdvisedResponse
- AdvisedRequest: LLM 호출 전에 구성된 프롬프트 데이터를 나타냄.
- AdvisedResponse: LLM 응답을 나타냄.
> 이들은 advisor chain 내에서 상태를 공유하기 위해 advise-context 포함.

## 주요 메서드
### nextAroundCall() / nextAroundStream()
프롬프트 데이터를 검사하거나 커스터마이징하거나 수정한 뒤 다음 advisor 호출 또는 LLM 호출
> 예외 발생 가능 (처리 실패 시)

### getOrder()
체인 내 실행 순서를 결정.
> 값이 작을수록 먼저 실행됨.

### getName()
어드바이저 고유 이름 반환.

## 어드바이저 체인 동작 특징
- Spring AI가 자동으로 Advisor Chain을 구성함.
- 여러 advisor들이 getOrder() 값 기준으로 순차적으로 실행됨.
- 마지막에는 LLM 호출이 자동으로 붙음.

## Advisor chain -> Chat Model 흐름
![img.png](images/advisor_chain_to_chat_model_flow.png)
1. Spring AI는 사용자의 Prompt를 기반으로 `AdvisedRequest`와 비어 있는 `AdvisorContext`를 생성함.
2. Advisor 체인의 각 advisor는 AdvisedRequest를 처리하며, 필요 시 내용을 수정할 수 있음.
   > advisor는 다음 advisor를 호출하지 않고 요청을 차단할 수도 있음. 이 경우 advisor가 응답을 직접 작성해야 함.
3. 마지막 advisor(프레임워크가 제공)는 실제 Chat Model에 요청을 보냄.
4. Chat Model의 응답은 AdvisedResponse로 변환되고, AdvisorContext와 함께 advisor 체인을 거슬러 올라가며 전달됨.
5. 각 advisor는 응답을 가공하거나 처리할 수 있음.
6. 최종적으로 클라이언트는 AdvisedResponse에서 ChatCompletion을 추출해 받게 됨.

## Advisor 실행 순서
getOrder() 메서드로 advisor 실행 순서를 결정함.
<br/>숫자가 작을수록 먼저 실행됨.

### 체인 동작 방식
- 요청 처리 시: 순서가 낮은 advisor부터 순차 실행됨.
- 응답 처리 시: 요청의 역순으로 처리됨  
   >먼저 실행된 advisor가 가장 나중에 응답을 처리함 (스택처럼 작동).

### 실행 순서 제어 팁
- 가장 먼저 요청을 처리하고 싶다면 `Ordered.HIGHEST_PRECEDENCE`에 가깝게 설정.
- 가장 마지막에 요청을 처리하고 싶다면 `Ordered.LOWEST_PRECEDENCE`에 가깝게 설정.
> 같은 order 값을 가지는 여러 advisor는 실행 순서가 보장되지 않음.


# API Overview
Advisor를 구성하는 핵심 인터페이스 구조
> `org.springframework.ai.chat.client.advisor.api`에 위치
## Advisor
핵심 인터페이스로서 Ordered를 상속받은 구조
```java
public interface Advisor extends Ordered {

	String getName();

}
```

## CallAroundAdvisor
동기적인 Advisor
```java
public interface CallAroundAdvisor extends Advisor {

	/**
	 * Around advice that wraps the ChatModel#call(Prompt) method.
	 * @param advisedRequest the advised request
	 * @param chain the advisor chain
	 * @return the response
	 */
	AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain);

}
```
## CallAroundAdvisorChain
Advisor의 chain을 지속하기 위한 인터페이스
```java
public interface CallAroundAdvisorChain {

	AdvisedResponse nextAroundCall(AdvisedRequest advisedRequest);

}
```

## StreamAroundAdvisor
Streaming advisor
```java
public interface StreamAroundAdvisor extends Advisor {

	/**
	 * Around advice that wraps the invocation of the advised request.
	 * @param advisedRequest the advised request
	 * @param chain the chain of advisors to execute
	 * @return the result of the advised request
	 */
	Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain);

}
```
## StreamAroundAdvisorChain
Advisor의 chain을 지속하기 위한 인터페이스
```java
public interface StreamAroundAdvisorChain {

	Flux<AdvisedResponse> nextAroundStream(AdvisedRequest advisedRequest);

}
```

# Advisor 구현하기
Advisor를 생성하기 위해서는 `CallAroundAdvisor`또는 `StreamAroundAdvisor`를 구현해야됨
> 핵심 구현 메서드는 `nextAroundCall()`또는 `nextAroundStream()`

## Logging Advisor (샘플)
`advisedRequest` 전에 로그를 생성하고, 다음 advisor 호출 후 `advisedResponse` 로그를 생성하는 advisor 샘플
```java
public class SimpleLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

	private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

	@Override
	public String getName() { 
        /** advisor의 고유 이름 정의 */
		return this.getClass().getSimpleName();
	}

	@Override
	public int getOrder() { 
        /** advisor 실행 순서 설정 */
		return 0;
	}

	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

		logger.debug("BEFORE: {}", advisedRequest);

		AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

		logger.debug("AFTER: {}", advisedResponse);

		return advisedResponse;
	}

	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

		logger.debug("BEFORE: {}", advisedRequest);

		Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

        /** MessageAggregator는 Flux 응답을 하나의 AdvisedResponse로 모아주는 유틸리티 클래스. */
        return new MessageAggregator().aggregateAdvisedResponse(advisedResponses,
                    advisedResponse -> logger.debug("AFTER: {}", advisedResponse)); 
	}
}
```

## Re-Reading (Re2) Advisor (샘플)
> Re-Reading : 동일한 질문을 한 번 더 프롬프트에 삽입함으로써 모델의 주의 집중과 문제 이해를 향상시키는 방식

```java
public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {


    /** 사용자의 input query에 Re2를 적용하는 메서드 */
	private AdvisedRequest before(AdvisedRequest advisedRequest) {

		Map<String, Object> advisedUserParams = new HashMap<>(advisedRequest.userParams());
		advisedUserParams.put("re2_input_query", advisedRequest.userText());

		return AdvisedRequest.from(advisedRequest)
			.userText("""
			    {re2_input_query}
			    Read the question again: {re2_input_query}
			    """)
			.userParams(advisedUserParams)
			.build();
	}

    /** non-streaming 에서 Re2 적용 */
	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
		return chain.nextAroundCall(this.before(advisedRequest));
	}
    
    /** streaming 에서 Re2 적용 */
	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
		return chain.nextAroundStream(this.before(advisedRequest));
	}

    /** 순서 설정 */
	@Override
	public int getOrder() {
		return 0;
	}

    /** 고유 이름 설정 */
    @Override
    public String getName() {
		return this.getClass().getSimpleName();
	}
}
```

# Built in Advisor
Spring AI에 내장된 Advisor

## Chat Memory Advisors
Chat Memory Store에서 대화 기록을 관리하는 Advisor

### MessageChatMemoryAdvisor
메모리를 검색하여 Prompt에 메시지 컬렉션 형태로 추가하여 대화 흐름의 구조를 유지

### PromptChatMemoryAdvisor
메모리를 검색하여 프롬프트의 system 메시지(system text)안에 통합

### VectorStoreChatMemoryAdvisor
Vector store의 메모리를 검색하여 system 메시지(system text)에 추가
> 거대한 데이터 속에서 관련있는 데이터를 효율적으로 검색하기에 용이함

## Question Answering Advisor
질의응답 관련 Advisor
### QuestionAnswerAdvisor
RAG패턴을 구현하여, 벡터 스토어를 활용한 질의응답 기능을 제공

## Content Safety Advisor
### SafeGuardAdvisor
모델이 유해하거나 부적절한 응답 생성을 방지

# Streaming vs Non-Streaming
![img.png](images/streaming_vs_non_streaming_in_advisor.png)
- Non streaming advisor는 완료된 요청과 응답에서 동작
- Streaming advisor는 지속되는 요청과 응답을 다룸
   > Reactive Programing 컨셉 사용
```java
@Override
public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

    return  Mono.just(advisedRequest)
            .publishOn(Schedulers.boundedElastic())
            .map(request -> {
                // This can be executed by blocking and non-blocking Threads.
                // Advisor before next section
            })
            .flatMapMany(request -> chain.nextAroundStream(request))
            .map(response -> {
                // Advisor after next section
            });
}
```

# Best Practice
- 역할 분리: 각 advisor는 하나의 명확한 기능만 수행하도록 구성.
- 상태 공유: 필요한 경우 `adviseContext`를 통해 advisor 간 정보를 공유.
- 유연한 대응: 스트리밍과 일반 응답 방식 둘 다 구현하여 다양한 상황에 대응.
- 순서 고려: advisor 실행 순서를 신중하게 설정하여 데이터 흐름이 자연스럽도록 하라.