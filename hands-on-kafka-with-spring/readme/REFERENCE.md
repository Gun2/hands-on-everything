# Spring for Apache Kafka reference 정리
> https://docs.spring.io/spring-kafka/reference

# Connecting to Kafka
> https://docs.spring.io/spring-kafka/reference/kafka/connecting.html
## 주요 컴포넌트
- KafkaAdmin: 토픽 자동 생성/관리 담당 → topic 생성 전략에 따라 중요
- ProducerFactory: KafkaTemplate이 사용하는 실제 Producer 생성기
- ConsumerFactory: Listener들이 사용하는 Consumer 생성기

> 핵심 변화 (v2.5 기준): 이들 모두 KafkaResourceFactory를 상속
> <br/>setBootstrapServersSupplier(() -> ...) 통해 동적으로 bootstrap servers 교체 가능

# Configuring Topics
> https://docs.spring.io/spring-kafka/reference/kafka/configuring-topics.html

## KafkaAdmin을 통한 토픽 자동 등록
KafkaAdmin 빈을 정의하면, Spring Context가 시작될 때 토픽을 자동으로 생성 가능
> Spring Boot 사용 시: KafkaAdmin은 자동 등록되므로 NewTopic만 작성해도 됨
> <br/> 실제 Kafka CLI의 kafka-topics.sh 옵션과 1:1로 매핑됨
```java
@Bean
public NewTopic topic1() {
    return TopicBuilder.name("thing1")
            .partitions(10)
            .replicas(3)
            //압축 옵션
            .compact()
            .build();
}
//다중 선언도 가능
@Bean
public KafkaAdmin.NewTopics topics456() {
    return new NewTopics(
            TopicBuilder.name("defaultBoth")
                    .build(),
            TopicBuilder.name("defaultPart")
                    .replicas(1)
                    .build(),
            TopicBuilder.name("defaultRepl")
                    .partitions(3)
                    .build());
}
```

# Sending Messages
> https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html

## KafkaTemplate 기본 사용법
- 메시지 전송 API는 모두 CompletableFuture<SendResult>를 반환 (비동기 방식)
- 메시지를 만들고 send()로 전송
- sendDefault(...) 계열은 기본 토픽이 설정되어 있어야 사용 가능
- ProducerRecord 또는 Message<?>도 지원
- 결과는 SendResult로 받고, 실패 시 KafkaProducerException 발생

### 비동기 예시
```java
public void async() {
    CompletableFuture<SendResult<String, String>> future = template.send("topic", "data");
    future.whenComplete((result, ex) -> { ... });
}
```
### 동기 예시
```java
public void sync() {
    template.send(record).get(10, TimeUnit.SECONDS);
}
```

## KafkaTemplate 설정
> Spring Boot에서는 자동 설정되는 ProducerFactory를 활용 가능
> <br/> KafkaTemplate에 ProducerListener를 설정하여 전송 결과 콜백 가능
```java
@Bean
public KafkaTemplate<Integer, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
}
```

