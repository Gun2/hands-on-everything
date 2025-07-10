# Kafka Retry
> https://www.baeldung.com/spring-retry-kafka-consumer
<br/> Kafka 컴슈머에서 재시도(Retry) 처리 방법

# Ack Mode
Ack Mode는 메시지 소비 후 오프셋을 커밋하는 시점을 제어
> 재시도 구현에는 RECORD 또는 MANUAL 모드가 적합
- RECORD: 메시지마다 개별 커밋
- BATCH: 배치 단위로 커밋
- TIME: 일정 시간 단위 커밋
- COUNT: N개 처리 후 커밋
- MANUAL: 직접 커밋 호출
```java
@Bean
public ConcurrentKafkaListenerContainerFactory<String, Object> greetingKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    // Other configurations
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
    factory.afterPropertiesSet();
    return factory;
}
```

# Backoff 전략
## Fixed Backoff
- 매 재시도 사이에 항상 같은 시간(예: 3000ms)을 기다림.
- 설정 예시: new FixedBackOff(3000L, 5) -> 3초 간격으로 5번까지 시도

# Exponential Backoff
- 재시도할수록 대기 시간이 증가함. (1초 -> 2초 -> 4초 -> 8초 ...)


# Blocking Retry vs Non Blocking Retry

## Blocking Retry
Kafka Consumer가 메시지 처리 중 일시적인 오류(e.g. 네트워크 타임아웃, 일시적인 DB 오류 등)를 만났을 때 동기적으로 재시도하는 방식

### Config
Backoff가 설정된 ErrorHandler를 KafkaListenerContainerFactory에 설정하며 bean으로 생성
```java
@Configuration
public class BlockingRetryConsumerConfig {

    public static final String CONFIG_BEAN_NAME = "blockingRetryKafkaListenerContainerFactory";

    @Value("${kafka.backoff.interval:1000}")
    private long interval;

    @Value("${kafka.backoff.max_failure:3}")
    private long maxAttempts;

    /**
     * backoff 설정한 ErrorHandler 생성
     * @return
     */
    @Bean
    public DefaultErrorHandler errorHandler() {
        BackOff backOff = new FixedBackOff(interval, maxAttempts);
        return new DefaultErrorHandler((record, ex) -> {
            System.out.println("⚠️ All retries exhausted for: " + record.value());
        }, backOff);
    }

    @Bean(name = CONFIG_BEAN_NAME)
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        //ErrorHandler 세팅
        factory.setCommonErrorHandler(errorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }
}
```
### Listener
retry 에러 핸들러가 설정된 containerFactory 지정
```java
    //containerFactory 지정
    @KafkaListener(topics = TOPIC_NAME, groupId = "test-group", containerFactory = BlockingRetryConsumerConfig.CONFIG_BEAN_NAME)
    public void listen(String message) {
        ...
    }
```

## Non Blocking Retry
메시지 처리가 실패해도 현재 Listener를 블로킹하지 않고, 실패한 메시지를 재시도용 토픽(Retry Topic) 으로 전송해 비동기적으로 다시 처리
> 실패한 메시지를 DLT(Dead Letter Topic)로 보낼 수 있음

### Listener
```java

    @RetryableTopic(
            backoff = @Backoff(value = 1000L),
            attempts = "10",    //시도 횟수 (넘으면 DLT로 빠짐)
            autoCreateTopics = "true"   //토픽 자동 생성 여부
    )
    @KafkaListener(topics = TOPIC_NAME, groupId = "test-group")
    public void listen(String message) {
        ...
    }

    //DTL에 빠질 경우 핸들링
    @DltHandler
    public void handleDlt(String message) {
        System.out.println("dlt : " + message);
    }
```

## 비교
| 항목        | **Blocking Retry** | **Non-Blocking Retry**  |
|-----------|--------------------|-------------------------|
| **방식**    | 재시도 중 멈추고 기다림      | 실패 메시지를 따로 저장 후 나중에 재시도 |
| **속도**    | 느려질 수 있음           | 빠름 (다른 메시지는 계속 처리)      |
| **순서 보장** | 잘 됨                | 깨질 수 있음                 |
| **설정**    | 단순                 | 복잡함 (전략적인 처리 필요)        |
| **자원 사용** | 대기 시간 있음           | 효율적                     |
| **추천 상황** | 순서 중요 / 간단한 재시도    | 성능 중요 / 다양한 실패 처리 필요    |
