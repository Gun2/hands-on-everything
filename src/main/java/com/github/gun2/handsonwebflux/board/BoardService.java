package com.github.gun2.handsonwebflux.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    public final BoardRepository boardRepository;
    public Mono<BoardDto> findById(Long id) {

        return boardRepository.findById(id).map(BoardDto::new);
    }

    public Flux<BoardDto> findAll(){
        return boardRepository.findAll().map(BoardDto::new);
    }

    @Transactional
    public Mono<BoardDto> create(BoardDto.BoardRequest dto) {
        return boardRepository.save(
                Board.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .createdAt(Instant.now())
                        .build()
        ).map(BoardDto::new);
    }

    @Transactional
    public Mono<BoardDto> update(BoardDto.BoardRequest dto, Long id) {
        return boardRepository.findById(id).map(board -> {
            board.updateBoard(dto.getTitle(), dto.getContent());
            return boardRepository.save(board).map(BoardDto::new);
        }).flatMap(boardDtoMono -> boardDtoMono);
    }

    public Mono<Void> delete(Long id) {
        return boardRepository.deleteById(id);
    }
}
