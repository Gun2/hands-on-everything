import { Body, Controller, Delete, Get, Param, Patch, Post, Query } from '@nestjs/common';
import { Board } from './board.entity';
import { BoardService } from './board.service';
import { BoardIdParams, CreateBoardRequest, UpdateBoardRequest } from './board.dto';

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
    getBoardById(
      @Param() params: BoardIdParams,
    ): Board | null {
        return this.boardService.getBoardById(params.id) || null;
    }

    @Post()
    createBoard(
      @Body() request: CreateBoardRequest,
    ): Board {
        return this.boardService.createBoard(request.title, request.content);
    }

    @Delete('/:id')
    deleteBoard(
      @Param() params: BoardIdParams,
    ): void {
        return this.boardService.deleteBoard(params.id);
    }

    @Patch('/:id')
    updateBoard(
        @Param() params: BoardIdParams,
        @Body() request: UpdateBoardRequest,
    ): Board | null{
        return this.boardService.updateBoard(params.id, request.title, request.content);
    }
}
