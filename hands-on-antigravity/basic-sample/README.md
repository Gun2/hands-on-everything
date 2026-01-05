# Bulletin Board Project (게시판 서비스)

이 프로젝트는 Spring Boot 백엔드와 React 프론트엔드로 구성된 기본적인 게시판 웹 애플리케이션입니다.
> Antigravity Agent를 통해 README가 생성됨

## 🛠 기술 스택 (Tech Stack)

### Backend
- **Framework**: Spring Boot 3
- **Language**: Java
- **Build Tool**: Gradle
- **Database**: H2 Database (In-memory)
- **Dependencies**: Spring Web, Spring Data JPA, Lombok

### Frontend
- **Library**: React
- **Build Tool**: Vite
- **Styling**: Vanilla CSS (Responsive Design)
- **HTTP Client**: Axios (예정)

## 📂 프로젝트 구조 (Project Structure)

```
/
├── backend/    # Spring Boot Application
├── frontend/   # React Application
└── README.md   # Project Documentation
```

## 🚀 실행 방법 (Getting Started)

이 프로젝트는 Backend와 Frontend 서버를 각각 실행해야 합니다.

### Prerequisites
- JDK 17 이상
- Node.js 18 이상

### Backend (Spring Boot)
1. `backend` 디렉토리로 이동합니다.
2. Gradle Wrapper를 사용하여 애플리케이션을 실행합니다.

```bash
cd backend
./gradlew bootRun
```

- 서버가 정상적으로 실행되면 `http://localhost:8080`에서 접근 가능합니다.

### Frontend (React)
1. `frontend` 디렉토리로 이동합니다.
2. 의존성을 설치하고 개발 서버를 실행합니다.

```bash
cd frontend
npm install
npm run dev
```

- 서버가 실행되면 터미널에 표시된 주소(기본: `http://localhost:5173`)로 접속하여 웹 애플리케이션을 사용할 수 있습니다.

## 📡 API Endpoints

Base URL: `/api/posts`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET    | `/`      | 모든 게시글 조회 | N/A          |
| GET    | `/{id}`  | 특정 게시글 조회 | N/A          |
| POST   | `/`      | 게시글 작성     | JSON (Post)  |
| PUT    | `/{id}`  | 게시글 수정     | JSON (Post)  |
| DELETE | `/{id}`  | 게시글 삭제     | N/A          |



## 📝 Antigravity Prompt Log

이 섹션은 Antigravity Agent와 상호작용한 기록(프롬프트 및 피드백)을 담고 있습니다.

### 1. Initial Request
> "기본적인 게시판 기능을하는 웹 애플리케이션을 만들거야. 게시판 기능 API를 제공하는 Spring boot app과 화면을 제공하는 react로 만든 UI 앱으로 게시판 서비스를 할거고, UI는 데스크톱, 테블릿, 모바일 사이즈에 대한 반응형 디자인이 적용되어야해"

### 2. Implementation Plan Review Feedback
사용자 피드백을 반영하여 계획을 수정하였습니다.

- **Build Tool 변경**: "gradle로 만들어줘"
  - *Action*: Maven에서 Gradle로 변경.
- **Test Coverage 요청**: "각 애플리케이션에 적절한 테스트 케이스 생성해줘"
  - *Action*: Backend (JUnit/Mockito for Controller, Service, Repository) 및 Frontend (React Testing Library) 테스트 계획 추가.
- **Documentation 요청**: "README.md에 프로젝트에 대한 설명을 작성해줘 구성과 api, 사용 스택 등 프로젝트에 대한 설명이 들어가야해, 그리고 antigravity에 대한 사용을 연습하는 프로젝트이기 때문에 내가 어떤 prompt를 전달했고 어떤 코멘트를 적었는지 같이 설명이 들어가야해 한국어로"
  - *Action*: 현재 보고 계신 README.md 파일 작성.

### 3. Import Error Investigation
> "Explain what this problem is and help me fix it: The import org.springframework.boot.test.autoconfigure.orm cannot be resolved ..."

- **Analysis**: IDE에서 `org.springframework.boot.test.autoconfigure.orm` 패키지를 찾지 못하는 문제 발생.
- **Verification**: 터미널에서 `./gradlew compileTestJava` 및 `./gradlew test` 실행 결과 성공. 의존성 설정(`spring-boot-starter-test`) 및 코드는 정상임을 확인.
- **Resolution**: 코드 수정 없이, IDE의 Gradle 프로젝트 리프레시 문제로 진단 및 안내.
