# 모듈 개요
외부 공통 이벤트를 받아 내부 이벤트로 전환하여 발행하는 리스너

# 활용 방법
## 의존성 추가
```groovy
dependencies {
    ...
	implementation project(':lib:external-event-adapter')
    ...
}

```