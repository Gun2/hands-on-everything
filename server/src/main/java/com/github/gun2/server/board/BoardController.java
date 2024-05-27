package com.github.gun2.server.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    public final BoardService boardService;

    @GetMapping("/{id}")
    public BoardDto readById(
            @PathVariable("id")Long id
            ){
        return boardService.findById(id);
    }

    @PostMapping
    public BoardDto create(
        @RequestBody BoardDto.BoardRequest dto
    ){
        return boardService.create(dto);
    }

    @PutMapping("/{id}")
    public BoardDto update(
            @PathVariable("id")Long id,
            @RequestBody BoardDto.BoardRequest dto
    ){
        return boardService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(
            @PathVariable("id")Long id
    ){
        boardService.delete(id);
        return true;
    }

    @GetMapping
    public Page<BoardDto> search(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
            ){
        return boardService.search(page, size);
    }
}
