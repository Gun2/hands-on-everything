import { HttpException, HttpStatus, Injectable, NotFoundException } from '@nestjs/common';
import { Board } from './board.entity';
@Injectable()
export class BoardService {
    private boards: Board[] = [];
    private idCounter = 1;

    getAllBoards(): Board[] {
        return this.boards;
    }

    getBoardById(id: number): Board {
        const board = this.boards.find(board => board.id == id);
        if (!board){
            throw new NotFoundException()
        }
        return board;
    }

    createBoard(title: string, description: string): Board {
        const newBoard: Board = {
            id: this.idCounter++,
            title,
            content: description,
            createdAt: new Date,
            updatedAt: new Date,
        };
        this.boards.push(newBoard);
        return newBoard;
    }

    deleteBoard(id: number): void {
        this.boards = this.boards.filter(board => board.id !== id);
    }

    updateBoard(id: number, title: string, description: string): Board {
        const board = this.getBoardById(id);
        if (board == undefined) {
            throw new NotFoundException()
        }
        board.title = title;
        board.content = description;
        board.updatedAt = new Date();
        return board;
    }

    search(page: number, size: number) {
        const startIndex = (page - 1) * size;
        return this.boards.slice(startIndex, startIndex + size);
    }
}
