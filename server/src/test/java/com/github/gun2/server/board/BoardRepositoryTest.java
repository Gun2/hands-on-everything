package com.github.gun2.server.board;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Slf4j
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void crudTest() {

        /**
         * CREATE AND READ
         */
        /** given */
        Board generatedBoard = generate();

        /** when */
        Board savedBoard = boardRepository.save(generatedBoard);
        Optional<Board> byId = boardRepository.findById(savedBoard.getId());

        /** then */
        assertThat(byId).isNotEmpty();
        Board createdBoard = byId.get();
        assertThat(createdBoard.getContent()).isEqualTo(generatedBoard.getContent());

        /**
         * UPDATE
         */
        /** when */
        createdBoard.updateContent("change");
        boardRepository.save(createdBoard);
        Optional<Board> updatedBoardOptional = boardRepository.findById(savedBoard.getId());

        /** then */
        assertThat(updatedBoardOptional).isNotEmpty();
        assertThat(updatedBoardOptional.get().getContent()).isEqualTo(createdBoard.getContent());

        /**
         * DELETE
         */
        /** when */
        boardRepository.deleteById(createdBoard.getId());
        Optional<Board> deletedBoardOptional = boardRepository.findById(createdBoard.getId());

        /** then */
        assertThat(deletedBoardOptional).isEmpty();
    }

    Board generate(){
        return Board.builder()
                .content("testtesttest")
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void pagingTest() {
        /** given */
        int totalSize = 101;
        int pageSize = 10;
        List<Board> boardList = Stream.generate(() -> generate()).limit(totalSize).toList();
        boardRepository.saveAll(boardList);

        /** when */
        Page<Board> page = boardRepository.findAll(PageRequest.of(1, pageSize, Sort.by("id").descending()));

        /** then */
        assertThat(page.getTotalElements()).isEqualTo(totalSize);
        assertThat(page.getTotalPages()).isEqualTo(11);
        assertThat(page.getContent().size()).isEqualTo(pageSize);
    }
}