package com.github.gun2.server.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.gun2.server.TestDataMutationUtil.asJsonString;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BoardControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    BoardRepository boardRepository;
    final String URL = "/boards";

    @Test
    void get() throws Exception {
        /** given */
        Board board = boardRepository.save(
                Board.builder()
                        .title("test")
                        .build()
        );
        /** when */
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/" + board.getId())
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        /** then */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(board.getTitle()))
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    void post() throws Exception {
        /** given */
        BoardDto.BoardRequest boardRequest = new BoardDto.BoardRequest();
        boardRequest.setTitle("test");
        boardRequest.setContent("content");

        /** when */
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(asJsonString(boardRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        /** then */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(boardRequest.getTitle()))
                .andExpect(jsonPath("$.content").value(boardRequest.getContent()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void put() throws Exception {
        /** given */
        Board board = boardRepository.save(
                Board.builder()
                        .title("test")
                        .build()
        );
        BoardDto.BoardRequest boardRequest = new BoardDto.BoardRequest();
        boardRequest.setTitle("change title");
        boardRequest.setContent(board.getContent());

        /** when */
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL + "/" + board.getId())
                        .content(asJsonString(boardRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        /** then */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()))
                .andExpect(jsonPath("$.title").value(boardRequest.getTitle()));
    }

    @Test
    void delete() throws Exception {
        /** given */
        Board board = boardRepository.save(
                Board.builder()
                        .title("test")
                        .build()
        );

        /** when */
        mockMvc.perform(
                MockMvcRequestBuilders.delete(URL + "/" + board.getId())
        ).andDo(print());
        Optional<Board> byId = boardRepository.findById(board.getId());

        /** then */
        assertThat(byId).isEmpty();
    }

    @Test
    void search() throws Exception {
        /** given */
        int total = 101;
        List<Board> savedBoardList = boardRepository.saveAll(
                Stream.generate(() -> Board.builder()
                        .title("title").content("content").createdAt(Instant.now())
                        .build()).limit(total).toList()
        );

        /** when */
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(URL)
                        .param("size", "10")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        /** then */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title"))
                .andExpect(jsonPath("$.totalElements").value(total))
                .andExpect(jsonPath("$.totalPages").value(11));
    }
}