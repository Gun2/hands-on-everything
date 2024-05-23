package com.github.gun2.server.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    public final BoardRepository boardRepository;
    public BoardDto findById(Long id) {
        return new BoardDto(boardRepository.findById(id).orElseThrow());
    }

    @Transactional
    public BoardDto create(BoardDto.BoardRequest dto) {
        return new BoardDto(boardRepository.save(
                Board.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .createdAt(Instant.now())
                        .build()
        ));
    }

    @Transactional
    public BoardDto update(BoardDto.BoardRequest dto, Long id) {
        Board board = boardRepository.findById(id).orElseThrow();
        return new BoardDto(boardRepository.save(board.updateBoard(dto.getTitle(), dto.getContent())));
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDto> search(Integer page, Integer size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())).map(
                BoardDto::new
        );
    }
}
