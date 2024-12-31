package com.github.gun2.server.board;

import com.github.gun2.server.websocket.ChangeDataEvent;
import com.github.gun2.server.websocket.DataStreamingUpdateService;
import com.github.gun2.server.websocket.WebSocketDestination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final DataStreamingUpdateService dataStreamingUpdateService;

    public BoardDto findById(Long id) {
        return new BoardDto(boardRepository.findById(id).orElseThrow());
    }

    @Transactional
    public BoardDto create(BoardDto.BoardRequest dto) {
        BoardDto createdData = new BoardDto(boardRepository.save(
                Board.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .createdAt(Instant.now())
                        .build()
        ));
        dataStreamingUpdateService.boardStreamingUpdate(ChangeDataEvent.Type.CREATE, createdData);
        return createdData;
    }

    @Transactional
    public BoardDto update(BoardDto.BoardRequest dto, Long id) {
        Board board = boardRepository.findById(id).orElseThrow();
        BoardDto updatedData = new BoardDto(boardRepository.save(board.updateBoard(dto.getTitle(), dto.getContent())));
        dataStreamingUpdateService.boardStreamingUpdate(ChangeDataEvent.Type.UPDATE, updatedData);
        return updatedData;
    }

    public void delete(Long id) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()){
            boardRepository.deleteById(id);
            dataStreamingUpdateService.boardStreamingUpdate(ChangeDataEvent.Type.DELETE, new BoardDto(boardOptional.get()));
        }
    }

    public Page<BoardDto> search(Integer page, Integer size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())).map(
                BoardDto::new
        );
    }
}
