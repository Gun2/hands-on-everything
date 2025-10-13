# Retrieval Augmented Generation
> https://docs.spring.io/spring-ai/reference/1.1-SNAPSHOT/api/retrieval-augmented-generation.html

RAG(Retrieval Augmented Generation)는 LLM 모델의 한계를 극복하기위한 기술로서 LLM이 장문의 내용, 정확한 정보, 더 자연스런 문맥 흐름과 함께 응답을 도출할 수 있도록 도와주는 기술이다.

## 특징
Spring AI는 RAG를 모듈화된 구성으로 제공한다. 이로 인하여 커스텀 RAG Flow들을 구성하거나 `Advisor`를 사용하여 즉시 사용 가능한 Flow들을 구성할 수 있다.


# Advisor
Spring AI는 `Advisor` API를 통해 즉시 사용 가능한 공통 RAG Flow들을 제공한다.

## 의존성
이러한 `Advisor` 중 `QuestionAnswerAdvisor`또는 `VectorStoreChatMemoryAdvisor`를 사용하기 위해서는 아래의 의존성을 추가해야 한다.
```xml
<dependency>
   <groupId>org.springframework.ai</groupId>
   <artifactId>spring-ai-advisors-vector-store</artifactId>
</dependency>
```

## QuestionAnswerAdvisor
Vector Database는 AI Model이 알 지 못하는 데이터를 저장하고 있는다. 사용자가 AI Model에 질문할 때 `QuestionAnswerAdvisor`는 이러한 데이터를 활용할 수 있도록 Vector Database로부터 사용자 요청과 관련된 문서들을 가져오는 역할을 수행한다.
이렇게 Vector Database로부터 조회된 정보는 사용자 요청에 추가되어 AI 모델이 더 나은 응답을 생성할 수 있도록 돕는다.

### 사용 예시
Vector Databasee에 적재된 데이터가 존재한다면 아래와같이 ChatClient에 `QuestionAnswerAdvisor`인스턴스를 제공하여 사용할 수 있다.
```java
ChatResponse response = ChatClient.builder(chatModel)
        .build().prompt()
        .advisors(new QuestionAnswerAdvisor(vectorStore))
        .user(userText)
        .call()
        .chatResponse();
```

#### SearchRequest 사용 예시
위의 예시처럼 사용하면 `QuestionAnswerAdvisor`는 Vector Database의 모든 문서를 탐색하게된다. 유형에 따라 제약적인 탐색을 위해서 `SearchRequest`를 사용할 수 있다.
아래는 `SearchRequest`를 사용한 예시이다.
```java
var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
        //유사성 임계값이 0.8이상이고, 상위 6개를 조회
        .searchRequest(SearchRequest.builder().similarityThreshold(0.8d).topK(6).build())
        .build();
```
### 동적 필터 표현
런타임간 ChatClient로 prompt 전송 시 아래와 같이 필터링을 수행할 수 있다.
```java
ChatClient chatClient = ChatClient.builder(chatModel)
    .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
        .searchRequest(SearchRequest.builder().build())
        .build())
    .build();

String content = this.chatClient.prompt()
    .user("Please answer my question XYZ")
    .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type == 'Spring'"))
    .call()
    .content();
```

### 커스텀 템플릿
`QuestionAnswerAdvisor`는 Vector Database로부터 조회한 정보들로 사용자 prompt를 보강할 때 기본 템플릿을 사용한다.
이 템플릿은 `PromptTemplate` 오브젝트의 `.promptTemplate()`빌더 메서드를 사용하여 커스터마이징 할 수 있다.
```java
PromptTemplate customPromptTemplate = PromptTemplate.builder()
    .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
    .template("""
            <query>

            Context information is below.

			---------------------
			<question_answer_context>
			---------------------

			Given the context information and no prior knowledge, answer the query.

			Follow these rules:

			1. If the answer is not in the context, just say that you don't know.
			2. Avoid statements like "Based on the context..." or "The provided information...".
            """)
    .build();

    String question = "Where does the adventure of Anacletus and Birba take place?";

    QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
        .promptTemplate(customPromptTemplate)
        .build();

    String response = ChatClient.builder(chatModel).build()
        .prompt(question)
        .advisors(qaAdvisor)
        .call()
        .content();
```

## RetrievalAugmentationAdvisor
Spring AI는 자신만의 RAG Flow를 제작할 수 있는 라이브러리들을 포함하고있다. `RetrievalAugmentationAdvisor`는 모듈화된 구성을 통해 일반적인 Flow들을 구성할 수 있는 `Advisor` 구현체 이다.

### 의존성
```xml
<dependency>
   <groupId>org.springframework.ai</groupId>
   <artifactId>spring-ai-rag</artifactId>
</dependency>
```

### Sequential RAG Flows 개요
RAG에서 모델은 답변하기 전에 Vector Store에서 관련 문서를 검색해 참조한다.
#### 기본 사용법
유사도를 설정할 수 있으며 기본적으로 검색 결과가 비어있으면 답변하지 않는다.
```java
Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
        .documentRetriever(VectorStoreDocumentRetriever.builder()
                .similarityThreshold(0.50)
                .vectorStore(vectorStore)
                .build())
//         아래 옵션을 통해 빈 값에 대한 답변을 허용할 수 있음
//        .queryAugmenter(ContextualQueryAugmenter.builder()
//                .allowEmptyContext(true)
//                .build())
//        질문을 재작성 하거나 후처리할 수 있음
//        .queryTransformers(RewriteQueryTransformer.builder()
//                .chatClientBuilder(chatClientBuilder.build().mutate())
//                .build())
        .build();

String answer = chatClient.prompt()
        .advisors(retrievalAugmentationAdvisor)
        // 동적인 필터링 필요 시 아래처럼 사용할 수 있음
        //.advisors(a -> a.param(VectorStoreDocumentRetriever.FILTER_EXPRESSION, "type == 'Spring'"))
        .user(question)
        .call()
        .content();
```

# Modules
Spring AI는 모듈형 RAG 구성을 구현한다.

## Pre-Retrieval
Pre-Retrieval 모듈은 사용자 질문이 최적의 결과를 탐색할 수 있도록 가공하는 역할을 수행한다.

### Query Transformation
사용자 요청 중 빈약한 질문, 애매한 용어, 복잡한 어휘, 지원되지 않는 언어로부터 변환을 수행하는 컴포넌트로서 쿼리를 더욱 효율적으로 만든다.

### CompressionQueryTransformer
`CompressionQueryTransformer`는 대화 히스토리와 후속 질문을 압축 해서,
전체 문맥을 반영한 독립적인 단일 질문 으로 변환하는 도구다

```java
Query query = Query.builder()
        .text("And what is its second largest city?")
        .history(new UserMessage("What is the capital of Denmark?"),
                new AssistantMessage("Copenhagen is the capital of Denmark."))
        .build();

QueryTransformer queryTransformer = CompressionQueryTransformer.builder()
        .chatClientBuilder(chatClientBuilder)
        .build();

Query transformedQuery = queryTransformer.transform(query);
```

### RewriteQueryTransformer
`RewriteQueryTransformer`는 사용자 요청이 타겟팅된 시스템(Vector Database, 웹 검색 엔진)으로부터 더욱 최적의 결과값을 얻을 수 있도록 LLM을 사용하여 변환하는 도구이다.
> 사용자 요청에 필요 이상의 많은 문장이 있거나, 중의적이거나, 관련없는 내용이 포함될 때 유용함
```java
Query query = new Query("I'm studying machine learning. What is an LLM?");

QueryTransformer queryTransformer = RewriteQueryTransformer.builder()
        .chatClientBuilder(chatClientBuilder)
        .build();

Query transformedQuery = queryTransformer.transform(query);
```

### TranslationQueryTransformer
`TranslationQueryTransformer`는 LLM을 이용해 사용자의 질의를 임베딩 모델이 지원하는 언어로 자동 번역하는 Transformer다.
```java
Query query = new Query("Hvad er Danmarks hovedstad?");

QueryTransformer queryTransformer = TranslationQueryTransformer.builder()
        .chatClientBuilder(chatClientBuilder)
        .targetLanguage("english")
        .build();

Query transformedQuery = queryTransformer.transform(query);
```

### Query Expansion
입력된 질의를 여러 개의 변형 쿼리로 확장하는 컴포넌트

#### MultiQueryExpander
`MultiQueryExpander`는 하나의 질문을 유사하게 변형된 여러 질문으로 만들어 다양한 관점을 잡아낼 수 있도록 한다. 이러한 방식은 추가적인 정보와 관련된 결과 탐색 기회를 높혀준다.
```java
MultiQueryExpander queryExpander = MultiQueryExpander.builder()
    .chatClientBuilder(chatClientBuilder)
    .numberOfQueries(3)
//   원본 쿼리를 리스트에서 제외하려면 아래와 같이 사용
//  .includeOriginal(false)
    .build();
List<Query> queries = queryExpander.expand(new Query("How to run a Spring Boot app?"));
```

## Retrieval
Retrieval 모듈은 Vector store처럼 Data 시스템들을 조회하고 가장 연관된 문서를 찾는 역할울 수행한다.

### Document Search
검색 엔진, 벡터 스토어, 데이터베이스, 지식 그래프 등과 같은 기반 데이터 소스에서 문서를 검색하는 역할을 담당하는 컴포넌트이다.

#### VectorStoreDocumentRetriever
`VectorStoreDocumentRetriever`는 Vector Store로부터 문서를 검색하며 의미적으로 input 질의와 유사하다.
```java
DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
    .vectorStore(vectorStore)
    .similarityThreshold(0.73)
    .topK(5)
//     supplier 형태로 사용도 가능
//    .filterExpression(() -> new FilterExpressionBuilder()
    .filterExpression(new FilterExpressionBuilder()
        .eq("genre", "fairytale")
        .build())
    .build();
List<Document> documents = retriever.retrieve(new Query("What is the main character of the story?"));
```

### Document Join
여러 요청 또는 여러 데이터 소스를 단일 문서로 결합하는 컴포넌트이다.

#### ConcatenationDocumentJoiner
`ConcatenationDocumentJoiner`는 다중 쿼리, 다중 데이터 소스로부터 받은 문서들을 이어붙여 결합한다.
> 중복 문서가 있으면 첫 번째 문서만 유지하고 문서의 점수는 유지한다.

```java
Map<Query, List<List<Document>>> documentsForQuery = ...
DocumentJoiner documentJoiner = new ConcatenationDocumentJoiner();
List<Document> documents = documentJoiner.join(documentsForQuery);
```

## Post-Retrieval
Post-Retrieval모듈은 탐색한 문서들을 최선의 결과를 얻기 위해 처리하는 역할을 수행한다.

### Document Post-Processing
쿼리와 관련된 문서를 후처리하는 컴포넌트, 검색 후 문서를 그대로 쓰지 않고, 품질 향상을 위해 추가 처리 수행
> 이렇게 하면 모델이 핵심 정보만 보고 답변을 생성할 수 있어 효율과 정확도가 높아짐


## Generation
사용자 질의와 탐색한 문서 기반으로 최종 응답을 생성하기 위한 역할을 수행하는 모듈

### Query Augmentation
입력 쿼리에 추가 문맥 정보를 결합하여 LLM이 사용자 요청에 필요한 문맥으로 답변도록 도와주는 컴포넌트

#### ContextualQueryAugmenter
`ContextualQueryAugmenter`는 제공된 문서의 내용을 기반으로 사용자 질의를 보강한다.
```java
QueryAugmenter queryAugmenter = ContextualQueryAugmenter.builder()
//        기본적으로 탐색 결과가 없으면 LLM이 응답하지 않도록 하는데 아래 옵션을 통해 해제할 수 있다.
//        .allowEmptyContext(true)
        .build();
```

