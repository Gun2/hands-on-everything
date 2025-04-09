# 모듈 개요
security context는 해당 프로젝트에서 서비스 통신간 인가를 위해 passport를 검증하는 구성을 자동으로 설정함.


# 활용 방법
## 의존성 추가
```groovy
dependencies {
    ...
    implementation project(':lib:security-filter')
    ...
}
```

## application.yml
```yaml
app:
  security:
    passport:
      secret: ABCDEFGHIJKLMN1234567890ABCDEFGHIJKLMN1234567890 #secret key 입력

```

# Configuration

| Property        | YAML                         | Type   | Description   |
|-----------------|------------------------------|--------|---------------|
| Passport Secret | app.security.passport.secret | String | passport의 비밀키 |
