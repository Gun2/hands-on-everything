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
`app.module.ts`는 최상위 모듈이며 여타 다른 모듈처럼 `@Module`데코레이터로 정의할 수 있음
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

# 실행
아래의 명령어를 명령창에 입렿하여 애플리케이션을 실행시킬 수 있음
```shell
npm run start
```
만약 개발간 변경돠는 파일을 자동으로 재컴파일 하고 서버를 리로드 하고싶다면 아래의 명령어로 애플리케이션을 실행시킬 수 있음
```shell
npm run start:dev
```

# Controllers
컨트롤러는 들어오는 요청을 핸들링하고 응답을 사용자에게 반환하는 역할을 한다.
> 참고 : https://docs.nestjs.com/controllers
## 라우팅 샘플
사용자의 요청을 컨트롤러의 함수로 라우팅 하기 위해서는 `@Controller()`데코레이터가 사용됨 `@Controller()`데코레이터를 활용하여 아래와 같이
컨트롤러를 구현할 수 있음
```ts
import { Controller, Get } from '@nestjs/common';

@Controller('boards')
export class BoardController {
  ...
  @Get('/:id')
  getBoardById(
    @Param() params: BoardIdParams,
  ): Board | null {
    return this.boardService.getBoardById(params.id) || null;
  }
}
```

## 응답
Http Response를 반환하는 방법은 크게 두 가지가 있다

### 기본 (권장)
NestJS에서 권장하는 방법은 Javascript 객체 또는 배열을 반환하는 방법이고, 이는 자동으로 JSON으로 변환된다.
만약 응답 코드를 선언하고 싶다면 `@HttpCode()` 데코레이터를 사용할 수 있다.
> 기본적으로 요청 성공 시 HttpStatus는 200을 반환하고 POST 메서드일 경우 201이 반환된다.
```ts
@Controller('boards')
export class BoardController {
    ...
    @Get('/:id')
    @HttpCode(HttpStatus.OK)
    getBoardById(
      @Param() params: BoardIdParams,
    ): Board | null {
      return this.boardService.getBoardById(params.id) || null;
    }
}
```

### 라이브러리 사용
라이브러리 스펙을 사용하여 응답값을 정의할 수도 있다.
```ts
import { Response } from 'express';

@Controller('boards')
export class BoardController {
  ...
  @Get('/:id')
  getBoardById(
    @Param() params: BoardIdParams,
    @Res() response: Response,
  ): void {
    response.status(HttpStatus.OK).send(this.boardService.getBoardById(params.id) || null);
  }
}
```

## 컨트롤러 등록
모듈에 컨트롤러를 등록하여 애플리케이션 구성에 추가할 수 있다.
```ts
//board.module.ts
import { Module } from '@nestjs/common';
import { BoardController } from './board.controller';

@Module({
  controllers: [BoardController],
  providers: []
})
export class BoardModule {}
```

# Provider
프로바이더는 NestJS의 기본 컨셉이며, Javascript Class에 `@Injectable()`데코레이터를 선언하여 생성할 수 있다.
이렇게 생성된 프로바이더는 DI될 수 있으며 Nest의 IoC 컨테이너에 의해 관리된다.
> 참고 : https://docs.nestjs.com/providers
## Service Provider 생성 샘플
```ts
//board.service.ts
import { Injectable } from '@nestjs/common';

@Injectable()
export class BoardService {
  private boards: Board[] = [];
  getBoardById(id: number): Board | null {
    return this.boards.find(board => board.id == id) || null;
  }
}
```

## DI 방법
### 생성자 주입
생성자를 사용하여 주입받는 방법
```ts
//board.controller.ts
import { BoardService } from './board.service';

@Controller('boards')
export class BoardController {
  //생성자를 통해 주입
  constructor(private readonly boardService: BoardService) {}
  ...
}
```

### 프로퍼티 주입
`@Inject()` 데코레이터를 프로퍼티에 선언하여 주입하는 방식으로, 최상위 레벨의 프로바이더에게 의존하는 프로바이더를
super()로 매번 주입시켜주기 어려울 때 활용할 수 있음
```ts
//board.controller.ts
import { BoardService } from './board.service';
import { Inject } from '@nestjs/common';

@Controller('boards')
export class BoardController {
  //프로퍼티에 주입
  @Inject(BoardService)
  private readonly boardService: BoardService
  ...
}
```

## 프로바이더 등록
모듈에 프로바이더를 등록하여 애플리케이션 구성에 추가할 수 있다.
```ts
//board.module.ts
import { Module } from '@nestjs/common';
import { BoardService } from './board.service';


@Module({
  controllers: [...],
  providers: [BoardService]
})
export class BoardModule {}
```

# Middleware
Route Handler 전에 호출되는 함수이며, `request`와 `response`의 객체에 접근할 수 있다.

## Middleware 함수가 수행할 수 있는 작업
Nest middleware는 기본적으로 express middleware와 동일함
- 어떤 코드라도 실행 가능
- `request`, `response`객체 수정 가능
- `request`-`response` 순환 종료 가능
- 스텍에서 다음 middleware 함수 호출
- `request`-`response` 순환이 종료되지 않았다면 `next()`함수를 호출 하여야함 (호출하지 않으면 요청에 행이 걸림)

## Middleware 사용 예시
middleware는 `function` 또는 `@Injectable()`데코레이터를 사용한 class`에 정의할 수 있음
`class`는 `function`과 다르게 `NestMiddleware`인터페이스를 구현해야함
```ts
// board.middleware.ts
// middleware 정의
import { Injectable, NestMiddleware } from '@nestjs/common';

@Injectable()
export class BoardMiddleware implements NestMiddleware {
  use(req: any, res: any, next: (error?: any) => void): any {
    console.log("board request : ", res)
    console.log("board response : ", res)
    next()
  }
}
```

```ts
// app.module.ts
// middleware 적용
import { MiddlewareConsumer, Module, NestModule, RequestMethod } from '@nestjs/common';
import { BoardMiddleware } from './board/board.middleware';

@Module({
  ...
})
export class AppModule  implements NestModule{
  configure(consumer: MiddlewareConsumer): any {
    consumer.apply(BoardMiddleware).forRoutes(
      {path: "boards/**", method: RequestMethod.ALL},
      //BoardController, //컨트롤러 자체를 대상으로 설정 가능
    )
  }
}

```

# Exception filters
nest는 처리되지 않은 예외를 처리하기 위한 책임을 가지는 exception 계층이 있다. 만약 예외가 애플리케이션 코드에서
처리되지 않았다면, exception 계층은 해당 에러를 잡아서 사용자에게 적절한 응답을 전달한다.

## 기본 예외 던지기
Nest는 `HttpException` 클래스를 내장하고 있다. 전형적인 HTTP REST/GraphQL API 기반 애플리케이션에서  에러가 발생하였을 때 
`HttpException`클래스의 객체를 예외로 던지게 된다면 상황에 적절한 기본적인 응답을 생성하기에 용이하다.
<br/>`HttpException`클래스의 생성자는 두 개의 인자를 필수로 받아야한다.
- [필수] 첫 번째 인자 : 응답 body (string | object)
- [필수] 두 번째 인자 : http 상태코드
- [선택] 세 번째 인자 : 에러 발생 원인
```ts
//board.service.ts
export class BoardService {
  private boards: Board[] = [];

  getBoardById(id: number): Board {
    const board = this.boards.find(board => board.id == id);
    if (!board) {
      throw new HttpException("Not Found", HttpStatus.NOT_FOUND);
    }
    return board;
  }
}
```

## Custom exceptions
`HttpException`클래스를 상속받아 별도의 예외 클래스를 만들어 nest가 exception을 인식하여 처리할 수 있도록 할 수 있다.
```ts
export class ForbiddenException extends HttpException {
  constructor() {
    super('Forbidden', HttpStatus.FORBIDDEN);
  }
}
```

## Exception filters
exception 계층을 넘어 전반적으로 예외를 제어하고 싶을 때 `Exception filter`를 사용할 수 있다.
### Exception 필터 생성
`@Catch()` 데코레이터와 `ExceptionFilter`인터페이스를 구현하여 만들 수 있으며, catch(exception: HttpException, host: ArgumentsHost) 메서드를 정의하여 예외를 처리할 수 있다.
```ts
//http-exception.filter.ts

import { ExceptionFilter, Catch, ArgumentsHost, HttpException } from '@nestjs/common';
import { Request, Response } from 'express';

@Catch(HttpException) //잡을 예외 클래스 선언
export class HttpExceptionFilter implements ExceptionFilter {
  catch(exception: HttpException, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const request = ctx.getRequest<Request>();
    const status = exception.getStatus();

    response
      .status(status)
      .json({
        statusCode: status,
        timestamp: new Date().toISOString(),
        path: request.url,
      });
  }
}

```
### Exception 필터 적용
@UseFilters()데코레이터를 통해 적용할 Exception 필터를 선언할 수 있으며 함수 혹인 클래스에 선언할 수 있다.
> @UseFilters()데코레이터에 아무 필터도 선언하지 않으면 모든 필터가 동작됨
```ts
@Controller('boards')
export class BoardController {
  ...
  @UseFilters(HttpExceptionFilter)
  @Get('/:id')
  getBoardById(
    @Param() params: BoardIdParams,
  ): Board | null {
    return this.boardService.getBoardById(params.id) || null;
  }
}
```

### Exception 필터 전역 적용
```ts
import { HttpExceptionFilter } from './http-exception.filter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  ...
  app.useGlobalFilters(new HttpExceptionFilter())
  await app.listen(3000);
}
bootstrap();
```

# Pipes
`Pipe`는 컨트롤러 메서드가 살행되기 전 메서드에 전달될 인자값을 받아 처리할 수 있으며, 변환(transformation) 또는 유효성 검증(validation)에 사용할 수 있다.

## Pipe 특징
Nest의 Pipe는 다음과 같은 특징을 가짐
### 예외는 Exception filter에 의해 처리됨
`Pipe`는 예외 영역에서 실행되기 때문에 실행 중 에러가 발생하면 exception filter에 의해 처리되고 controller의 method를 실행시키지 않는다.
### 내장 Pipe
`Pipe`는 아래와 같이 다수의 내장 Pipe를 가지고 있음
<br/> `ValidationPipe`는 유효성 검증(validation) 관련 pipe이고 `Parse*Pipe`는 변환(transformation) 관련 파이프이다.
- ValidationPipe
- ParseIntPipe
- ParseFloatPipe
- ParseBoolPipe
- ParseArrayPipe
- ParseUUIDPipe
- ParseEnumPipe
- DefaultValuePipe
- ParseFilePipe

## Pipe 바인딩
Pipe를 사용하기 위해서는 바인딩을 시켜주어야하며, method에 결합하는 방식과, 전역으로 바인딩하는 방식이 있음
### method 결합 방식 (파라미터 level)
`@Param()`데코레이터에 Pipe의 클래스를 인자값으로 선언할 수 있음
```ts
@Controller('samples')
export class SampleController {
  @Get(':id')
  async findOne(@Param('id', ParseIntPipe) id: number) {
    return this.catsService.findOne(id);
  }
}
```
아래와 같이 설정값으로 생성자를 통해 생성된 인스턴스로 선언할 수도 있음
```ts
@Controller('samples')
export class SampleController {
  @Get(':id')
  async findOne(
    @Param('id', new ParseIntPipe({ errorHttpStatusCode: HttpStatus.NOT_ACCEPTABLE }))
      id: number,
  ) {
    return this.catsService.findOne(id);
  }
}
```

### method 결합방식 (핸들러 level)
`@UsePipes()` 데코레이터를 사용하여 바인딩 할 수 있음
```ts
//board.controler.ts
@Controller('boards')
export class BoardController {
    @UsePipes(new ValidationPipe({
      enableDebugMessages: true
    }))
    @Post()
    createBoard(
      @Body() request: CreateBoardRequest,
    ): Board {
      return this.boardService.createBoard(request.title, request.content);
    }
}
```

### 전역 바인딩
useGlobalPipes 함수를 통해 `Pipe`를 전역으로 바인딩할 수 있음
```ts
//main.ts
async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe());
  await app.listen(process.env.PORT ?? 3000);
}
bootstrap();
```

# Guards
guard는 주어진 request가 route handler에 의해 처리될지 말지를 결정할 수 있는 역할을 가지고 있으며 주로 `인가`기능 구현에 사용됨
> - middleware와 다른점은 midddleware는 next() 함수 호출 후 어떤 핸들러가 오는지 모르지만 `guard`는 `ExecutionContext` 인스턴스를 통해 접근할 수 있음
> - guard는 모든 middleware 동작 후 동작됨

> 참고 : https://docs.nestjs.com/guards
> 
## Guard 구현 방법
`guard`를 구현하기 위해 `CanActivate` 인터페이스를 구현할 수 있다. 
<br/>`canActivate()` 함수의 반환값 여부로 handler의 동작 유무를 결정할 수 있다.
```ts
import { Injectable, CanActivate, ExecutionContext } from '@nestjs/common';
import { Observable } from 'rxjs';

@Injectable()
export class RolesGuard implements CanActivate {
  canActivate(
    context: ExecutionContext,
  ): boolean | Promise<boolean> | Observable<boolean> {
    //true이면 allow, false이면 deny
    return true;
  }
}
```

## Guard 적용 방법
`guard`도 `pipe`, `exception filter`처럼 controller, method, global 범위에 바인딩할 수 있다.
### 컨트롤러 범위에 바인딩
`@UseGuards()` 데코레이터를 사용하여 컨트롤러에 바인딩할 수 있다.
> 특정 메서드에 적용하고 싶다면 메서드 위에 `@UseGuards()` 데코레이터를 사용할 수 있다.
```ts
@Controller('cats')
@UseGuards(RolesGuard)
export class CatsController {}
```
### 전역 범위에 바인딩
Nest application 인스턴스의 useGlobalGuards 메서드를 통해 전역으로 `guard`를 바인딩할 수 있다.
```ts
const app = await NestFactory.create(AppModule);
app.useGlobalGuards(new RolesGuard());
```
### 모듈에 바인딩
Nest application 인스턴스의 useGlobalGuards 메서드를 통해 전역으로 `guard`를 바인딩을 한 경우
해당 바인딩은 모듈 밖에서 등록되기 때문에 DI를 활용할 수 없게된다. 이러한 문제를 해결하기 위해서 모듈에 바인딩을 할 수 있다.
```ts
import { Module } from '@nestjs/common';
import { APP_GUARD } from '@nestjs/core';

@Module({
  providers: [
    {
      provide: APP_GUARD,
      useClass: RolesGuard,
    },
  ],
})
export class AppModule {}
```

## 역할 인가 구현 예시
인가 과정이 필요한 핸들러에 사용될 커스텀 데코레이터를 생성하고 적용하는 방법
### 커스텀 데코레이터를 생성
```ts
//roles.decorator.ts
import { Reflector } from '@nestjs/core';

export const Roles = Reflector.createDecorator<string[]>();
```
### 인가 데코레이터 적용
```ts
@Post()
@Roles(['admin'])
async create(@Body() createCatDto: CreateCatDto) {
  this.catsService.create(createCatDto);
}
```
### 인가 처리할 guard 작성
```ts
//roles.guard.ts
import { Injectable, CanActivate, ExecutionContext } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { Roles } from './roles.decorator';

@Injectable()
export class RolesGuard implements CanActivate {
  constructor(private reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean {
    const roles = this.reflector.get(Roles, context.getHandler());
    if (!roles) {
      return true;
    }
    const request = context.switchToHttp().getRequest();
    const user = request.user;
    return matchRoles(roles, user.roles);
  }
}
```

# Interceptors
Interceptor는 AOP(Aspect Oriented Programming)에 영감을 두었기에 다음과 같은 특징을 가지고 있다.
- method 실행 전후의 추가 로직 바인드
- function으로 부터 반환된 결과 변환
- function으로 부터 발생된 예외 반환
- 기본 function 동작의 확장
- 특정 조건ㅇ의 function의 완전한 재정의

## 구현 메서드 intercept()
Interceptor는 intercept() method를 구현하여야만 하며 `ExecutionContext`인스턴스와 `CallHandler`인스턴스를 인자로 받을 수 있다.
### ExecutionContext
현재 실행 과정에 대한 상세 정보를 담고 있음

### CallHandler
handle()메서드를 구현하였으며 라우터 핸들러 메서드를 인터셉터에서 특정 시점에 호출할 수 있음

## Interceptor 예시
`NestInterceptor`인터페이스를 구현하여 Interceptor를 만들 수 있다.
<br/> intercept()메서드는 Observable을 반환하여야 한다.
```ts
//logging.interceptor.ts
import { Injectable, NestInterceptor, ExecutionContext, CallHandler } from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class LoggingInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    console.log('Before...');

    const now = Date.now();
    return next
      .handle()
      .pipe(
        tap(() => console.log(`After... ${Date.now() - now}ms`)),
      );
  }
}
```

## Interceptor 바인딩
### 컨트롤러에 바인딩
`@UseInterceptors`데코레이터를 사용하여 컨트롤러에 `interceptor`를 바인딩할 수 있으며 메서드에도 적용할 수 있음
```ts
@UseInterceptors(LoggingInterceptor)
export class CatsController {}
```
### 전역 바인딩
애플리케이션 인스턴스의 `useGlobalInterceptors`메서드를 통해 전역으로 바인딩 할 수 있음
```ts
const app = await NestFactory.create(AppModule);
app.useGlobalInterceptors(new LoggingInterceptor());
```
### 모듈에 바인딩
providers에 값을 추가하여 바인딩할 수 있음
```ts
import { Module } from '@nestjs/common';
import { APP_INTERCEPTOR } from '@nestjs/core';

@Module({
  providers: [
    {
      provide: APP_INTERCEPTOR,
      useClass: LoggingInterceptor,
    },
  ],
})
export class AppModule {}
```

# 사용 예시

## 유효성 검증 구현하기

> 참고 : https://docs.nestjs.com/techniques/validation

### 라이브러리 설치
```shell
npm i --save class-validator class-transformer
```

### Auto-Validation (전역 설정)
`main.ts`의 앱에 `ValidationPipe`를 바인딩하여 모든 엔드포인트에 유효성 검증을 적용할 수 있음
```ts
async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe()); //binding
  await app.listen(3000);
}
bootstrap();
```

### Method level validation (개별 설정)
유효성 검증을 적용하고자 하는 함수 상단에 `@UsePipes()` 데코레이터를 사용하여 개별적으로 선언할 수 있음
```typescript
@Controller('boards')
export class BoardController {
    @UsePipes(new ValidationPipe({
      enableDebugMessages: true
    }))
    @Post()
    createBoard(
      @Body() request: CreateBoardRequest,
    ): Board {
      return this.boardService.createBoard(request.title, request.content);
    }
}
```