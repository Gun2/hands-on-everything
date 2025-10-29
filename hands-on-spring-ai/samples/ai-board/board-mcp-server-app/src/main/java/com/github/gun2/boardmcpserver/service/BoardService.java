package com.github.gun2.boardmcpserver.service;

import com.github.gun2.boardmcpserver.dto.BoardRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class BoardService {

    private final RestClient boardClient;

    public BoardService(RestClient.Builder builder) {
        this.boardClient = builder.baseUrl("http://localhost:8080/boards").build();
    }

    @Tool(description = "Get a list of all board posts.")
    public String getBoardList() {
        return boardClient.get()
                .uri(uriBuilder -> uriBuilder
                        .build())
                .retrieve()
                .body(String.class);
    }

    @Tool(description = "Get details of a specific board post by ID.")
    public String getBoardDetail(Long id) {
        return boardClient.get()
                .uri("/{id}", id)
                .retrieve()
                .body(String.class);
    }

    @Tool(description = "Create a new board post with title and content.")
    public String createBoardPost(String title, String content) {
        return boardClient.post()
                .body(BoardRequest.builder()
                        .title(title)
                        .content(content)
                        .build())
                .retrieve()
                .body(String.class);
    }

    @Tool(description = "Update an existing board post by ID.")
    public String updateBoardPost(Long id, String title, String content) {
        return boardClient.put()
                .uri("/{id}", id)
                .body(BoardRequest.builder()
                        .title(title)
                        .content(content)
                        .build())
                .retrieve()
                .body(String.class);
    }

    @Tool(description = "Delete a board post by ID.")
    public String deleteBoardPost(Long id) {
        return boardClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity().getStatusCode().is2xxSuccessful() ? "id : "  + id + " is deleted successfully" : "id : "  + id + " is not deleted";
    }

}
