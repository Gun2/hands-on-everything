# Spring Cloud Function
서버리스 아키텍처와 마이크로서비스 아키텍처에서 코드를 재사용하고 유연하게 배포할 수 있도록 돕는 프레임워크

# Quick Start
## 의존성 추가
```groovy
dependencies {
    ...
    implementation 'org.springframework.cloud:spring-cloud-starter-function-web'
    ...
}

```

## 함수 등록 (Bean 등록 방식)
```java
@SpringBootApplication
public class HandsOnSpringCloudFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandsOnSpringCloudFunctionApplication.class, args);
    }


    /**
     * 문자열 뒤집어 반환
     * @return
     */
    @Bean
    public Function<String, String> reverseString() {
        return value -> new StringBuilder(value).reverse().toString();
    }

    /**
     * data 값 만큼 count 값 증가
     * @return
     */
    @Bean
    public Consumer<Integer> increaseCount(){
        return count::getAndAdd;
    }

    /**
     * 현재 count값 반환
     * @return
     */
    @Bean
    public Supplier<String> getCount(){
        return count::toString;
    }
}
```

## 함수 실행
```shell
# 문자열 뒤집기
curl http://127.0.0.1:8080/reverseString -H "Content-Type: text/plain" -d "ABCDEFG"
# output : GFEDCBA

# 현재 카운트 값
curl http://127.0.0.1:8080/getCount -H "Content-Type: text/plain"
# output : 0

# 카운트값 증가
curl http://127.0.0.1:8080/increaseCount -H "Content-Type: text/plain" -d 5

# 현재 카운트 값
curl http://127.0.0.1:8080/getCount -H "Content-Type: text/plain"
# output : 5
```

# 참고
https://docs.spring.io/spring-cloud-function/docs/current/reference/html/spring-cloud-function.html
https://www.baeldung.com/spring-cloud-function