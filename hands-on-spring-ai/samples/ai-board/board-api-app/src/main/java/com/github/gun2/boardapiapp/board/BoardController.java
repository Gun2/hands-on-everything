package com.github.gun2.boardapiapp.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;

    @GetMapping("")
    ResponseEntity<List<Board>> getBoards() {
        return ResponseEntity.ok(boardRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Board> getBoard(@PathVariable Long id) {
        return ResponseEntity.ok(boardRepository.findById(id).orElseThrow());
    }

    @PostMapping("")
    ResponseEntity<Board> createBoard(@RequestBody BoardRequest request) {
        Board createdBoard = boardRepository.save(Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @PutMapping("/{id}")
    ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody BoardRequest request) {
        Board board = boardRepository.findById(id).orElseThrow();
        board.update(request.getTitle(), request.getContent());
        Board updatedBoard = boardRepository.save(board);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
