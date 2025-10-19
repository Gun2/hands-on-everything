# Chat server
해당 프로젝트는 AI 채팅 기능을 제공하는 백엔드 애플리케이션이다.

# API
지원하는 API

| Method | Path     | Description  |
|--------|----------|--------------|
| Post   | /chat-ai | AI에게 프롬프트 질의 |

## chat api
### AI 채팅 입력
동기 방식으로 사용자의 프롬프트를 처리하여 응답함
```http request
curl --location --request POST 'http://localhost:8080/chat-ai' \
--header 'Content-Type: application/json' \
--data-raw '{
    "content": "Can you make me fun?"
}'
```