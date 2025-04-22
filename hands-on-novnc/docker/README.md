# 설명
VNC 접근 테스트를 위한 GUI를 지원하는 칼리리눅스 컨테이너 생성

# VNC 내장된 컨테이너 실행 방법
## 이미지 빌드
```shell
docker build -t kali-gui .
```

## Kali 컨테이너 실행
```shell
docker run -d -p 5901:5901 -p 6080:6080 --name kali-desktop kali-gui
```

## VNC 접근
```shell
vnc://localhost:5901
# password : kali
```

## noVnc 접근
```
host : localhost
port : 6080
password : kali
```