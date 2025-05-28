# 프로젝트 개요
Spring을 사용하여 Kafka를 다루는 프로젝트

# 프로젝트 구조
```shell
.
├── HELP.md
├── README.md
├── ...
├── docker
│   └── docker-compose.yml  # kafka 서비스 구동 docker compose
├── ...
└── src # 샘플 코드
    ├── main
    └── test
```

## 데이터 흐름
스프링 앱은 `간단한 채팅 앱 처럼 동작하며` API 요청의 채팅 메시지를 kafka에게 전달함.
이벤트를 처리하기위해 스프링 앱은 kafka Topic을 구독하여 이벤트 발생 시 채팅 메시지를 DB에 저장하는 흐름을 가짐
![standard_architecture.jpg](readme%2Fstandard_architecture.jpg)

## API
해당 앱에서 사용 가능한 API
### 채팅 보내기
```http request
POST /chats HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "message" : "chat message"
}
```
```shell
curl --location --request POST 'http://localhost:8080/chats' \
--header 'Content-Type: application/json' \
--data-raw '{
    "message" : "chat message"
}'
```

### 채팅 내역 확인
```http request
GET /chats HTTP/1.1
Host: localhost:8080
```
```shell
curl --location --request GET 'http://localhost:8080/chats'
```

### 컨슈머 목록 조회
```http request
GET /kafka/consumers HTTP/1.1
Host: localhost:8080
```
```shell
curl --location --request GET 'http://localhost:8080/kafka/consumers'
```

### 컨슈머 조회
```http request
GET /kafka/consumers/{CONSUMER_ID} HTTP/1.1
Host: localhost:8080
```
```shell
CONSUMER_ID=CONSUMER_ID_01
curl --location --request GET "http://localhost:8080/kafka/consumers/${CONSUMER_ID}"
```

### 컨슈머 실행
```http request
POST /kafka/consumers/{CONSUMER_ID}/start HTTP/1.1
Host: localhost:8080
```
```shell
CONSUMER_ID=CONSUMER_ID_01
curl --location --request POST "http://localhost:8080/kafka/consumers/${CONSUMER_ID}/start"
```

### 컨슈머 중지
```http request
POST /kafka/consumers/{CONSUMER_ID}/stop HTTP/1.1
Host: localhost:8080
```
```shell
CONSUMER_ID=CONSUMER_ID_01
curl --location --request POST "http://localhost:8080/kafka/consumers/${CONSUMER_ID}/stop"
```



# Kafka 제어
kafka 구성을 위해 docker-compose를 사용하여 kafka를 실행하고 중지 하는 방법
## Kafka 실행
docker compose를 통해 kafka 실행
```shell
docker-compose -f ./docker/docker-compose.yml up -d
```

## Kafka 중지
docker compose를 통해 kafka 중지
```shell
docker-compose -f ./docker/docker-compose.yml down
```

# 구성
## 기본 구성
하나의 토픽에 하나의 컨슈머를 연결하여 소비하는 구성
### 프로듀서 추가
```java
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String message) {
        kafkaTemplate.send(topic,  message);
    }
}
```

### 컨슈머 추가
```java

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatListener {
    private final ChatService chatService;

    @KafkaListener(topics = Topics.CHAT_TOPIC, groupId = "demo-group")
    public void listen(String message) {
        chatService.createChat(message);
    }
}
```

## 컨슈머 분산 구성
하나의 토픽에 2개의 파티션을 구성하고 2개의 컨슈머가 처리하는 방식
> 발행할 파티션 선정은 라운드로빈 방식으로 발행

![distribution_consumer_architecture.jpg](readme%2Fdistribution_consumer_architecture.jpg)
### 카프카 토픽 생성 설정
```java
@Configuration
@Slf4j
public class KafkaConfig {
    @Bean
    NewTopic newTopic(){
        return TopicBuilder.name(Topics.CHAT_TOPIC)
                //2개의 파티션 생성
                .partitions(2)
                .replicas(1)
                .build();
    }
}

```
### 프로듀서 구성
```java
@Service
public class KafkaProducerService {
    private final AtomicLong increment = new AtomicLong();
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String message) {
        //key값을 0,1로 번갈아서 발행
        kafkaTemplate.send(topic, String.valueOf(increment.getAndIncrement() % 2),  message);
    }
}
```
### 컨슈머 구성
2개의 컨슈머 생성
```java
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatListener {
    private final ChatService chatService;

    @KafkaListener(topics = Topics.CHAT_TOPIC, groupId = "demo-group")
    public void listen(String message) {
        log.info("[consumer1]listen : {}", message);
        chatService.createChat(message, "consumer1");
    }

    @KafkaListener(topics = Topics.CHAT_TOPIC, groupId = "demo-group")
    public void listen2(String message) {
        log.info("[consumer2]listen : {}", message);
        chatService.createChat(message, "consumer2");
    }
}

```