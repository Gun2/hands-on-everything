package com.github.gun2.server.websocket;

import com.github.gun2.server.board.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * data 변경 정보 streaming 서비스
 */
@Service
@RequiredArgsConstructor
public class DataStreamingUpdateService {
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * board 변경 정보 전파
     * @param type
     * @param boardDto
     */
    public void boardStreamingUpdate(ChangeDataEvent.Type type, BoardDto boardDto){
        messagingTemplate.convertAndSend(WebSocketDestination.BOARD, new ChangeDataEvent<BoardDto>(type, boardDto));
    }
}
