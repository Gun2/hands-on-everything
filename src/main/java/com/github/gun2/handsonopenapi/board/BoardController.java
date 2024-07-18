package com.github.gun2.handsonopenapi.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    public final BoardService boardService;

    @Operation(summary = "id로 게시글 정보 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardDto.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않음",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public BoardDto readById(
            @PathVariable("id")Long id
            ){
        return boardService.findById(id);
    }

    @Operation(summary = "게시글 생성")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "생성 성공",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardDto.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content
            )
    })
    @PostMapping
    public BoardDto create(
        @Validated @RequestBody BoardDto.BoardRequest dto
    ){
        return boardService.create(dto);
    }

    @Operation(summary = "게시글 수정")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardDto.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public BoardDto update(
            @PathVariable("id")Long id,
            @Validated @RequestBody BoardDto.BoardRequest dto
    ){
        return boardService.update(dto, id);
    }

    @Operation(summary = "게시글 삭제")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public Boolean delete(
            @PathVariable("id")Long id
    ){
        boardService.delete(id);
        return true;
    }

    @GetMapping("/filter")
    public Page<BoardDto> search(
            @ParameterObject Pageable pageable
            ){
        return boardService.search(pageable.getPageNumber(), pageable.getPageSize());
    }
}
