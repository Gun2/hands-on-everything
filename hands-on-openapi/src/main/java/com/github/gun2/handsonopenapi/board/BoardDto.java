package com.github.gun2.handsonopenapi.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
        @Size(max = 50)
        private String title;
        @Size(max = 2000)
        private String content;
    }
}
