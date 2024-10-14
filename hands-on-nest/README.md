# Hands On Nest
NestJS 핸즈온 프로젝트

# NestJS란
NestJS는 Node.js를 통해 효율적이고 확장 가능한 서버 사이드 애플리케이션을 구성할 수 있도록 도와주는 프레임워크이다.

## NestJS 특징

### 동작 언어
javascript를 기반으로 동작되며 typescript의 사용이 권장됨

### Node.js 프레임워크
NestJS는 Node.js 프레임워크인 Express(default)와 Fastify를 사용할 수 있음
<br/> 위의 두 프레임워크(Express / Fastify)는 NestJS가 추상화 레벨에서 제공하기에 변경이 용이한편

# 설치
NestJS 설치 방법
## Nest 설치 준비
`Nest CLI`를 사용하여 NestJS 프로젝트를 쉽게 생성할 수 있으며 아래 명령어를 통해 설치할 수 있음
```shell
npm i -g @nestjs/cli
nest -v
```
## Nest 프로젝트 생성
`Nest CLI`가 설치되었다면 아래의 명령어를 통해 NestJS 프로젝트를 생성
```shell
# javascript 사용 시 --strict를 생략
nest new --strict `project-name`
# project 생성 후 의존성 설치 진행
cd `project-name`
npm install
```

### Nest 초기 생성 구조
NestJS 프로젝트를 설치하면 `src` 디렉터리가 아래와 같은 구조를 가지고 있음
```shell
src
├── app.controller.spec.ts
├── app.controller.ts
├── app.module.ts
├── app.service.ts
└── main.ts
```
| 파일명                    | 역할                                    |
|------------------------|---------------------------------------|
| main.ts                | 애플리케이션을 구동하기 위한 인스턴스를 생성하는 로직이 포함되어있음 |
| app.controller.ts      | 단일 라우터의 기본 컨트롤러                       |
| app.controller.spec.ts | 컨트롤러에 대한 유닛 테스트                       |
| app.service.ts         | 단일 메서드의 기본 서비스                        |
| app.module.ts          | NestJS 애플리케이션의 구성 요소들을 설정하고 관리하는 데 사용 |

### NestJS의 module
Nest의 모듈은 애플리케이션의 구성요소들을 설정하는 역할을 담당하고 있다. 그 중 프로젝트 생성 초기에 생성되어있는
`app.module.ts`파일은 최상단 모듈로서의 역할을 수행하며, 계층적인 구조를 통해 애플리케이션의 구성을 설정할 수 있다.

#### app.module.ts
`app.module.ts`는 최상위 모듈이며 여타 다른 모듈처럼 `@Module`데코레이션으로 정의할 수 있음
```ts

@Module({
  imports: [],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
```
| key         | 역할                               |
|-------------|----------------------------------|
| imports     | 애플리케이션 구성 요소 설정에 포함될 하위 모듈들이 입력됨 |
| controllers | 컨트롤러들이 입력됨                       |
| providers   | 서비스 들이 입력됨                       |

## Test

```bash
# unit tests
$ npm run test

# e2e tests
$ npm run test:e2e

# test coverage
$ npm run test:cov
```

## Support

Nest is an MIT-licensed open source project. It can grow thanks to the sponsors and support by the amazing backers. If you'd like to join them, please [read more here](https://docs.nestjs.com/support).

## Stay in touch

- Author - [Kamil Myśliwiec](https://kamilmysliwiec.com)
- Website - [https://nestjs.com](https://nestjs.com/)
- Twitter - [@nestframework](https://twitter.com/nestframework)

## License

Nest is [MIT licensed](LICENSE).
