package com.github.gun2.handsonwebflux.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    public final BoardRepository boardRepository;
    public Mono<BoardDto> findById(Long id) {

        return Mono.just(new BoardDto(boardRepository.findById(id).orElseThrow()));
    }

    public Flux<BoardDto> findAll(){
        return Flux.fromStream(
                StreamSupport.stream(boardRepository.findAll().spliterator(), false).map(BoardDto::new)
        );
    }

    @Transactional
    public Mono<BoardDto> create(BoardDto.BoardRequest dto) {
        return Mono.just(new BoardDto(boardRepository.save(
                Board.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .createdAt(Instant.now())
                        .build()
        )));
    }

    @Transactional
    public Mono<BoardDto> update(BoardDto.BoardRequest dto, Long id) {
        Board board = boardRepository.findById(id).orElseThrow();
        return Mono.just(new BoardDto(
                boardRepository.save(board.updateBoard(dto.getTitle(), dto.getContent()))
        ));
    }

    public Mono<Void> delete(Long id) {
        boardRepository.deleteById(id);
        return Mono.empty();
    }
}
