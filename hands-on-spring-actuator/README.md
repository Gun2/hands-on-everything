# Spring Actuator란
Spring Actuator는 애플리케이션의 상태를 모니터링하고 관리할 수 있도록 도와주는 라이브러리

# Spring Actuator 기능
- 애플리케이션이 정상 동작 중인지 확인 (/actuator/health)
- 현재 사용 중인 환경 변수나 설정값 확인 (/actuator/env)
- 메모리 사용량, 스레드 상태 등 시스템 정보 확인 (/actuator/metrics)
- 애플리케이션에서 로드된 Bean 목록 조회 (/actuator/beans)
- 로그 레벨 변경 (/actuator/loggers)

## 주요 endpoint


### /actuator	
사용 가능한 Actuator 엔드포인트 목록 제공
```shell
curl http://localhost:8080/actuator
```
### /actuator/health	
애플리케이션의 상태 확인 (예: UP, DOWN)
```shell
curl http://localhost:8080/actuator/health	
```

### /actuator/info	
애플리케이션 정보 제공
```shell
curl http://localhost:8080/actuator/info
```

### /actuator/metrics
메모리 사용량, CPU 사용률 등 시스템 메트릭 정보 제공
```shell
curl http://localhost:8080/actuator/metrics
```

### /actuator/env	
애플리케이션 환경 변수 및 설정 정보 제공
```shell
curl http://localhost:8080/actuator/env
```

### /actuator/beans	
현재 애플리케이션에서 로드된 Bean 목록 제공
```shell
curl http://localhost:8080/actuator/beans
```

# 설정
## 의존성 추가
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    ...
}
```

## 환경설정 (application.properties)
```properties
# 기보적으로 /와 /health만 활성화 되기에 다른 엔드포인트를 활성화 시키기 위해 설정
management.endpoints.web.exposure.include=*
```
