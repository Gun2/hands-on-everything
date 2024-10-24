import { MiddlewareConsumer, Module, NestModule, RequestMethod } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { BoardModule } from './board/board.module';
import { BoardMiddleware } from './board/board.middleware';
import { APP_INTERCEPTOR } from '@nestjs/core';
import { LoggingInterceptor } from './logging.interceptor';

@Module({
  imports: [BoardModule],
  controllers: [AppController],
  providers: [
    AppService,
    {
      provide: APP_INTERCEPTOR,
      useClass: LoggingInterceptor,
    },
  ],
})
export class AppModule  implements NestModule{
  configure(consumer: MiddlewareConsumer): any {
    consumer.apply(BoardMiddleware).forRoutes(
      {path: "boards/**", method: RequestMethod.ALL},
      //BoardController,
    )
  }
}

