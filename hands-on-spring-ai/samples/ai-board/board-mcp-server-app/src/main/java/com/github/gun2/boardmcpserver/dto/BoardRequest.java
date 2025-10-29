package com.github.gun2.boardmcpserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequest {

    private Long id;

    private String title;

    private String content;
}
