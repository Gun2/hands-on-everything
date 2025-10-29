package com.github.gun2.boardmcpserver.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    private Long id;

    private String title;

    private String content;

    private Instant createdAt;

    private Instant updatedAt;

}
