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

## 함수 등록 (method @Bean 등록 방식)
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


## 함수 실행 (method @Bean 등록 방식)
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


## 함수 등록 (class bean 등록 방식)
```java
//bean 이름으로 url path를 지정할 수 있음
@Component(value = "reverse-string")
public class ReverseStringFunction implements Function<String, String> {
    @Override
    public String apply(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}

@Component
public class CountComponent extends AtomicLong {
}

@RequiredArgsConstructor
@Component(value = "increase-count")
public class IncreaseCountConsumer implements Consumer<Integer> {
    private final CountComponent countComponent;
    @Override
    public void accept(Integer count) {
        countComponent.getAndAdd(count);
    }
}

@Component(value = "get-count")
@RequiredArgsConstructor
public class GetCountSupplier implements Supplier<Long> {
    private final CountComponent countComponent;

    @Override
    public Long get() {
        return countComponent.get();
    }
}
```
## 함수 실행 (class bean 등록 방식)
```shell
# 문자열 뒤집기
curl http://127.0.0.1:8080/reverse-string -H "Content-Type: text/plain" -d "ABCDEFG"
# output : GFEDCBA

# 현재 카운트 값
curl http://127.0.0.1:8080/get-count -H "Content-Type: text/plain"
# output : 0

# 카운트값 증가
curl http://127.0.0.1:8080/increase-count -H "Content-Type: text/plain" -d 5

# 현재 카운트 값
curl http://127.0.0.1:8080/get-count -H "Content-Type: text/plain"
# output : 5
```

# 참고
- https://docs.spring.io/spring-cloud-function/docs/current/reference/html/spring-cloud-function.html
- https://www.baeldung.com/spring-cloud-function