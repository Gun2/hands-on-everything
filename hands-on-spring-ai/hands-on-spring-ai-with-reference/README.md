# Hands on spring ai with reference
Spring ai reference를 읽으며 진행하는 hands on 프로젝트
> https://docs.spring.io/spring-ai/reference/index.html

# 설정
해당 핸즈온 프로젝트를 구성하기 위한 설정
## gradle 설정
```groovy
ext {
    springAiVersion = "1.0.0"
}

dependencies {
    ...
    implementation 'org.springframework.ai:spring-ai-starter-model-ollama'
    implementation 'org.springframework.ai:spring-ai-vector-store'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.ai:spring-ai-bom:$springAiVersion"
    }
}
```

### properties
```properties
# 모델이 없을 때만 다운로드하도록 설정
spring.ai.ollama.init.pull-model-strategy=when_missing
# llama3.2 모델을 사용
spring.ai.ollama.chat.options.model=llama3.2
# ollama url 설정 (default : localhost:11434)
# spring.ai.ollama.base-url=localhost:11434
```
