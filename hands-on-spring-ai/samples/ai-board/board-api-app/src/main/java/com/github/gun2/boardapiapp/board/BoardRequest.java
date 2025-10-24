package com.github.gun2.boardapiapp.board;

import lombok.Data;

@Data
public class BoardRequest {

    private Long id;

    private String title;

    private String content;
}
