# Hands on next container with docker
docker를 통해 next app을 빌드/배포하는 프로젝트

## 프로젝트 구성
- next 16
- nodejs 22

## Docker 빌드/배포
Docker를 사용하여 Next앱 컨테이너화를 통해 빌드/배포하는 방법
### 빌드
```shell
docker build -t my-next-app:1.0 .
```

### 추출
```shell
docker save -o my-next-app.tar my-next-app:1.0
```

### 로드
```shell
docker load -i my-next-app.tar
```

### 실행
```shell
docker run -d -p 3000:3000 --name next-app -v $(pwd)/logs:/app/logs --env-file .env my-next-app:1.0
```

## 스크립트 사용
작성된 스크립트를 사용하여 빌드/배포/제어하는 방법
> next-service.sh 사용

### 빌드
```shell
./next-service.sh build
```

### 추출
```shell
./next-service.sh export
```

### 로드
```shell
./next-service.sh import my-next-app-1.0.tar
```

### 실행
```shell
./next-service.sh start
```

### 상태 확인
```shell
./next-service.sh status
```

### 중지
```shell
./next-service.sh stop
```

### 재실행
```shell
./next-service.sh restart
```