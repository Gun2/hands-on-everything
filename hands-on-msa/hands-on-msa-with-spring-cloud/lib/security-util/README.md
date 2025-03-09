# 모듈 개요
security module은 해당 프로젝트에서 공통적인 인증/인가를 처리하기 위한 유틸리티를 제공함

# 활용 방법
## 의존성 추가
```groovy
dependencies {
    ...
    implementation project(':lib:security-module')
    ...
}
```

# Configuration

| Property              | YAML                       | Type    | Description                      |
|-----------------------|----------------------------|---------|----------------------------------|
| Disable passport util | app.security.passport.util | boolean | passport util 사용 여부 (기본값 = true) |
