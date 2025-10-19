package com.github.gun2.chatserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class ClientChatResponse {
    private String answer;
}
