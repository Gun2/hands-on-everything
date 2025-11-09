# ---- 1단계: Build ----
FROM node:22.18-alpine3.22 AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# ---- 2단계: Run ----
FROM node:22.18-alpine3.22 AS runner

WORKDIR /app

ENV NODE_ENV=production

# 빌드 결과와 package.json 복사
COPY --from=builder /app/package*.json ./
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public

# 의존성 설치 (production 전용)
RUN npm ci --only=production

EXPOSE 3000
CMD sh -c "npm run start > /app/logs/out.log 2>&1"
