package com.github.gun2.handsonwebflux.board;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    private Long id;

    private String title;

    private String content;

    private Instant createdAt;

    public Board updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }
}
