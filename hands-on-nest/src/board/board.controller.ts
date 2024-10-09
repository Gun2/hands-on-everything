import { Body, Controller, Delete, Get, Param, Patch, Post, Query } from '@nestjs/common';
import { Board } from './board.entity';
import { BoardService } from './board.service';

@Controller('boards')
export class BoardController {

    @Get()
    getAllBoards(
      @Query("page") page: number = 1,
      @Query("size") size: number = 10,
    ): Board[] {
        return this.boardService.search(page, size);
    }

    constructor(private readonly boardService: BoardService) {}

    @Get('/:id')
    getBoardById(@Param('id') id: number): Board | null {
        return this.boardService.getBoardById(id) || null;
    }

    @Post()
    createBoard(
        @Body('title') title: string,
        @Body('content') description: string
    ): Board {
        return this.boardService.createBoard(title, description);
    }

    @Delete('/:id')
    deleteBoard(@Param('id') id: number): void {
        return this.boardService.deleteBoard(id);
    }

    @Patch('/:id')
    updateBoard(
        @Param('id') id: number,
        @Body('title') title: string,
        @Body('content') description: string
    ): Board | null{
        return this.boardService.updateBoard(id, title, description);
    }
}
