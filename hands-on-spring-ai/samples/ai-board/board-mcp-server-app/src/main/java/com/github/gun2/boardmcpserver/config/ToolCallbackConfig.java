package com.github.gun2.boardmcpserver.config;

import com.github.gun2.boardmcpserver.service.BoardService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolCallbackConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(BoardService boardService) {
        return MethodToolCallbackProvider.builder().toolObjects(boardService).build();
    }
}
