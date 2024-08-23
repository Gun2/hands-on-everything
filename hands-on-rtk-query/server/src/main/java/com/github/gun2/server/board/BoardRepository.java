package com.github.gun2.server.board;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, PagingAndSortingRepository<Board, Long> {

}

