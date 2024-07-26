package com.github.gun2.handsonwebflux.board;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends R2dbcRepository<Board, Long> {

}

