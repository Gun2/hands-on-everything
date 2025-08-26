# Chat Memory 

> https://docs.spring.io/spring-ai/reference/1.1-SNAPSHOT/api/chat-memory.html

# Chat Memory의 필요성
기본적으로 LLM들은 이전 상호작용을 유지하지 않는 Stateless하게 동작된다. 이러한 방식은 문맥을 유지하고 다양한 상호작용을 하는 것에 제한이 생기게 된다.
그래서 Spring AI 는 `Chat Memory`를 제공한다.

# Chat Memory 특징
다양한 User Case 별로 구현할 수 있도록 추상화를 제공한다. 
> Message의 유지 기간, 개수, 토큰 제한 등을 전략적으로 설정할 수 있음

# Chat Memory vs Chat History
## Chat Memory
현재 대화 맥락을 유지하기 위해 필요한 정보이며 모델이 "지금 상황"을 이해하도록 돕는다.

## Chat History
전체 대화 기록에 용이하며 모든 사용자, 모델 간 메시지를 보존한다.


# Memory Type
Chat Memory 추상화는 다양한 유형의 Use Case별로 적합하게 구현될 수 있는 특징을 가지고 있다.

## Message Window Chat Memory
`MessageWindowChaeMemory`는 특정 사이즈만큼 메시지의 window를 관리한다.
> 슬라이딩 윈도우 방식으로 특정 N개의 메시지를 보관하며 기본적으로 20개의 메시지를 유지한다.
> 참고로 ChatMemory에서 기본적으로 사용되는 bean이다.
```java
MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
        .maxMessages(10)
        .build();
```

# Memory Storage
Spring AI는 `ChaeMemoryRepository`추상화를 제공한다.

## InMemory Repository
`InMemoryRepository`는 메시지를 `CurrentHashMap`에 저장한다.
> 별 다른 Repository가 설정되지 않았을 때 사용되는 용도로 활용됨
```java
// 아래처럼 수동으로 생성할 수 있다.
ChatMemoryRepository repository = new InMemoryChatMemoryRepository();
```

## JdbcChatMemoryRepository
> [jdbc chat memory 샘플](./CHAT_MEMORY_JDBC.md)
`JdbcChatMemoryRepository` JDBC를 사용하여 다양한 DB에 Chat Message를 영속화할 수 있다.
```groovy
//의존성
dependencies {
    implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc'
}
```

### Auto configuration
Spring AI는 `JdbcChatMemoryRepository`의 Auto Configuration을 지원하기에 바로 사용이 가능하다.
```java
@Autowired
JdbcChatMemoryRepository chatMemoryRepository;

ChatMemory chatMemory = MessageWindowChatMemory.builder()
    .chatMemoryRepository(chatMemoryRepository)
    .maxMessages(10)
    .build();
```

### 수동 설정
`JdbcTemplate`을 사용하여 수동으로 `JdbcChatMemoryRepository`를 생성할 수 있다.
```java
ChatMemoryRepository chatMemoryRepository = JdbcChatMemoryRepository.builder()
    .jdbcTemplate(jdbcTemplate)
    .dialect(new PostgresChatMemoryRepositoryDialect())
    .build();

ChatMemory chatMemory = MessageWindowChatMemory.builder()
    .chatMemoryRepository(chatMemoryRepository)
    .maxMessages(10)
    .build();
```

### 지원 Database 및 방언
- PostgreSQL
- MySQL / MariaDB
- SQL Server
- HSQLDB
방언은 `JdbcChatMemoryRepositoryDialect.from(DataSource)`구문에서 JDBC URL을 통해 자동 감지되고, `JdbcChatMemoryRepositoryDialect` 인터페이스로 확장할 수 있다.

### Configuration
| Property                                                | Description                                                      | Default Value                                                                         | Example Scenario |
| ------------------------------------------------------- | ---------------------------------------------------------------- | ------------------------------------------------------------------------------------- |------------------|
| spring.ai.chat.memory.repository.jdbc.initialize-schema | 스키마를 언제 초기화할지 결정. 옵션: embedded(기본), always, never.               | embedded                                                                              | 초기화 설정           |
| spring.ai.chat.memory.repository.jdbc.schema            | 초기화에 사용할 스키마 SQL 파일 경로. classpath: URL 및 @@platform@@ 플레이스홀더 지원. | classpath\:org/springframework/ai/chat/memory/repository/jdbc/schema-@@platform@@.sql | 초기화 스키마 스크립트 위치  |
| spring.ai.chat.memory.repository.jdbc.platform          | 스크립트에서 @@platform@@ 플레이스홀더를 어떤 DB 플랫폼으로 치환할지 지정                  | auto-detected                                                                         | 초기화에 사용되는 플랫폼 설정 |

### 스키마 초기화
Spring AI의 JdbcChatMemoryRepository는 애플리케이션 시작 시 자동으로 SPRING_AI_CHAT_MEMORY 테이블을 생성.
> 기본적으로 embedded DB(H2, HSQL, Derby 등)에서만 실행.

#### 초기화 관련 설정
```properties
spring.ai.chat.memory.repository.jdbc.initialize-schema=embedded # 내장 DB에서만 초기화 (기본값)
spring.ai.chat.memory.repository.jdbc.initialize-schema=always   # 항상 초기화
spring.ai.chat.memory.repository.jdbc.initialize-schema=never    # 초기화하지 않음 (Flyway/Liquibase 사용 시 유용)
```

#### 스키마 스크립트 경로 변경
```properties
spring.ai.chat.memory.repository.jdbc.schema=classpath:/custom/path/schema-mysql.sql
```

#### DB 방언 확장
JdbcChatMemoryRepositoryDialect를 구현해 새로운 DB 지원 가능
```java
ChatMemoryRepository chatMemoryRepository = JdbcChatMemoryRepository.builder()
        .jdbcTemplate(jdbcTemplate)
        .dialect(new MyCustomDbDialect())
        .build();
```

## CassandraChatMemoryRepository
`CassandraChatMemoryRepository`는 메시지를 저장하기 위해 Apache Cassandra를 사용한다.

### 특징
- 가용성, 내구성, 확장성이 중요한 애플리케이션에 적합.
- TTL(Time-To-Live) 기능 활용 가능 (메시지 자동 만료 설정)
- 시계열(Time-Series) 스키마: 과거 모든 대화 기록 보존 (감사/거버넌스에 유용)

### 의존성
```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-cassandra'
}
```

### Auto Configuration
Spring은 CassandraChatMemoryRepository를 즉시 사용할 수 있도록 Auto Configuration 지원
```java
@Autowired
CassandraChatMemoryRepository chatMemoryRepository;

ChatMemory chatMemory = MessageWindowChatMemory.builder()
    .chatMemoryRepository(chatMemoryRepository)
    .maxMessages(10)
    .build();
```

### 수동 설정
CassandraChatMemoryConfig를 활용하여 수동으로 설정 가능 
```java
ChatMemoryRepository chatMemoryRepository = CassandraChatMemoryRepository
    .create(CassandraChatMemoryConfig.builder().withCqlSession(cqlSession));

ChatMemory chatMemory = MessageWindowChatMemory.builder()
    .chatMemoryRepository(chatMemoryRepository)
    .maxMessages(10)
    .build();
```

### Properties
| Property                                          | Description            | Default Value    |
| ------------------------------------------------- | ---------------------- | ---------------- |
| spring.cassandra.contactPoints                    | 클러스터 탐색을 위한 호스트        | 127.0.0.1        |
| spring.cassandra.port                             | Cassandra 네이티브 프로토콜 포트 | 9042             |
| spring.cassandra.localDatacenter                  | 연결할 Cassandra 데이터센터    | datacenter1      |
| spring.ai.chat.memory.cassandra.time-to-live      | Cassandra에 저장된 메시지 TTL | -                |
| spring.ai.chat.memory.cassandra.keyspace          | Cassandra Keyspace     | springframework  |
| spring.ai.chat.memory.cassandra.messages-column   | 메시지 컬럼 이름              | springframework  |
| spring.ai.chat.memory.cassandra.table             | Cassandra 테이블 이름       | ai\_chat\_memory |
| spring.ai.chat.memory.cassandra.initialize-schema | 시작 시 스키마 초기화 여부        | true             |

### 초기화
Auto configuration은 `ai_chat_memory`테이블을 자동으로 생성한다.
아래의 설정으로 초기화를 비활성화 할 수 있다.
```properties
spring.ai.chat.memory.repository.cassandra.initialize-schema to false.
```

## Neo4j ChatMemoryRepository
`Neo4jChatMemoryRepository`는 그래프 DB를 활용하여 노드간 관계를 저장하는 내장 구현체

### 의존성
```groovy
dependencies {
    implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-neo4j'
}
```

### Auto Configuration
```java
@Autowired
Neo4jChatMemoryRepository chatMemoryRepository;

ChatMemory chatMemory = MessageWindowChatMemory.builder()
    .chatMemoryRepository(chatMemoryRepository)
    .maxMessages(10)
    .build();
```

### 수동 설정
`Driver`인스턴스를 통해 수동 설정 가능
```java
ChatMemoryRepository chatMemoryRepository = Neo4jChatMemoryRepository.builder()
    .driver(driver)
    .build();

ChatMemory chatMemory = MessageWindowChatMemory.builder()
    .chatMemoryRepository(chatMemoryRepository)
    .maxMessages(10)
    .build();
```

### Properties
| Property                                                 | Description              | Default Value |
| -------------------------------------------------------- | ------------------------ | ------------- |
| spring.ai.chat.memory.repository.neo4j.sessionLabel      | 대화 세션을 저장할 노드 레이블        | Session       |
| spring.ai.chat.memory.repository.neo4j.messageLabel      | 메시지를 저장할 노드 레이블          | Message       |
| spring.ai.chat.memory.repository.neo4j.toolCallLabel     | 툴 호출 정보를 저장할 노드 레이블      | ToolCall      |
| spring.ai.chat.memory.repository.neo4j.metadataLabel     | 메시지 메타데이터를 저장할 노드 레이블    | Metadata      |
| spring.ai.chat.memory.repository.neo4j.toolResponseLabel | 툴 응답을 저장할 노드 레이블         | ToolResponse  |
| spring.ai.chat.memory.repository.neo4j.mediaLabel        | 메시지와 연관된 미디어를 저장할 노드 레이블 | Media         |

### 초기화
Neo4j repository는 자동으로 대화ID와 메시지에 인덱싱이 필요하면 생성한다.
따라서 별도의 초기화를 요구하지는 않는다.

# Memory in Chat Client
ChatClient API를 사용할 때 문맥을 유지하기 위하여 ChatMemory구현체를 제공해야한다. 

## 주요 Advisors
- MessageChatMemoryAdvisor : 메모리를 메시지 집합으로 불러와 프롬프트에 포함 (MessageWindowChatMemory와 함께 자주 사용됨)
- PromptChatMemoryAdvisor : 메모리를 일반 텍스트로 변환해 시스템 프롬프트에 추가. 커스텀 PromptTemplate 가능 (placeholder: instructions, memory).
- VectorStoreChatMemoryAdvisor: 벡터 스토어에서 과거 대화 기록을 검색 후 시스템 메시지에 추가. 커스텀 PromptTemplate 가능 (placeholder: instructions, long_term_memory).
> 제한: 툴 호출 시 모델과 주고받는 중간 메시지는 현재 메모리에 저장되지 않음. → 추후 개선 예정. 필요 시 User Controlled Tool Execution 방식 사용.

## 사용 예시
`MessageWindowChatMemory`와 `MessageChatMemoryAdvisor`를 사용하는 예시
```java
ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

ChatClient chatClient = ChatClient.builder(chatModel)
    .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
    .build();
```

## Memory in Chat Model
만약 ChatClient 대신 Chat Model을 사용하면 더욱 커스터마이징하고 효율적으로 관리할 수 있다.
```java
// Create a memory instance
ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
String conversationId = "007";

// First interaction
UserMessage userMessage1 = new UserMessage("My name is James Bond");
chatMemory.add(conversationId, userMessage1);
ChatResponse response1 = chatModel.call(new Prompt(chatMemory.get(conversationId)));
chatMemory.add(conversationId, response1.getResult().getOutput());

// Second interaction
UserMessage userMessage2 = new UserMessage("What is my name?");
chatMemory.add(conversationId, userMessage2);
ChatResponse response2 = chatModel.call(new Prompt(chatMemory.get(conversationId)));
chatMemory.add(conversationId, response2.getResult().getOutput());

// The response will contain "James Bond"
```