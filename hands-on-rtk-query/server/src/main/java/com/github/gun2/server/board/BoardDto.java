package com.github.gun2.server.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private Instant createdAt;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class BoardRequest {
        @NotBlank
        private String title;
        private String content;
    }

    public Board toDomain(){
        return Board.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .createdAt(this.createdAt)
                .build();
    }
}
