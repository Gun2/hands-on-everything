package com.github.gun2.handsonwebflux.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    public final BoardService boardService;

    @GetMapping
    public Flux<BoardDto> readAll(){
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BoardDto>> readById(
            @PathVariable("id")Long id
            ){
        return boardService.findById(id).map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<BoardDto>> create(
        @Validated @RequestBody BoardDto.BoardRequest dto
    ){
        return boardService.create(dto).map(
                boardDto -> ResponseEntity.status(HttpStatus.CREATED).body(boardDto)
        );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BoardDto>> update(
            @PathVariable("id")Long id,
            @Validated @RequestBody BoardDto.BoardRequest dto
    ){
        return boardService.update(dto, id).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("id")Long id
    ){
        return boardService.delete(id).map(ResponseEntity::ok);
    }
}
