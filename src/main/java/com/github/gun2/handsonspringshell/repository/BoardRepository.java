package com.github.gun2.handsonspringshell.repository;

import com.github.gun2.handsonspringshell.dto.Board;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BoardRepository {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, Board> store = new ConcurrentHashMap<>();

    public Board create(String title, String content){
        Board board = new Board(idGenerator.getAndIncrement(), title, content, LocalDateTime.now(), LocalDateTime.now());
        store.put(board.getId(), board);
        return board;
    }

    public Board update(Long id, String title, String content){
        if (!this.store.containsKey(id)){
            throw new NoSuchElementException("존재하지 않는 값 입니다. id : " + id);
        }
        Board board = store.get(id);
        board.update(title, content);
        return board;
    }

    public void delete(Long id){
        store.remove(id);
    }

    public Optional<Board> read(Long id){
        return Optional.ofNullable(
                this.store.getOrDefault(id, null)
        );
    }

    public List<Board> readAll(){
        return this.store.values().stream().toList();
    }
}
