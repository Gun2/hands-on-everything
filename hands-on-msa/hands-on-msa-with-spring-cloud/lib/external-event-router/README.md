# 모듈 개요
내부 이벤트를 외부 이벤트로 감싼 뒤 kafka에 발해하기 위한 컴포넌트

# 활용 방법
## 의존성 추가
```groovy
dependencies {
    ...
	implementation project(':lib:external-event-router')
    ...
}

```

## 샘플
```java

@RequiredArgsConstructor
@Component

public class Sample {
    private final EventRouter eventRouter;

    @Transactional
    public Object create(CreateRequest createRequest) {
        ...
        eventRouter.publish(new ExternalEvent(save));
        return save;
    }

}


```