import { MiddlewareConsumer, Module, NestModule, RequestMethod } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { BoardModule } from './board/board.module';
import { BoardMiddleware } from './board/board.middleware';
import { BoardController } from './board/board.controller';

@Module({
  imports: [BoardModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule  implements NestModule{
  configure(consumer: MiddlewareConsumer): any {
    consumer.apply(BoardMiddleware).forRoutes(
      {path: "boards/**", method: RequestMethod.ALL},
      //BoardController,
    )
  }
}

