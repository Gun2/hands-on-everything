import { Injectable, NestMiddleware } from '@nestjs/common';

@Injectable()
export class BoardMiddleware implements NestMiddleware {
  use(req: any, res: any, next: (error?: any) => void): any {
    console.log("board request : ", res)
    console.log("board response : ", res)
    next()
  }
}