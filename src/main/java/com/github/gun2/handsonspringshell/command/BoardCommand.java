package com.github.gun2.handsonspringshell.command;

import com.github.gun2.handsonspringshell.dto.Board;
import com.github.gun2.handsonspringshell.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BoardCommand {
    private final BoardRepository boardRepository;
    @ShellMethod(key = "create", prefix = "-", value = "게시글 생성")
    public String create(
            @ShellOption(
                    value = {"t", "title"},
                    help = "게시글의 제목을 입력합니다."
            ) String title,
            @ShellOption(
                    value = {"c", "content"},
                    help = "게시글의 내용을 입력합니다."
            ) String content

    ) {
        Board board = boardRepository.create(title, content);

        return """
                게시글을 작성하였습니다.
                %s""".formatted((boardToText(board)));
    }

    @ShellMethod(key = "read", prefix = "-", value = "id에 해당하는 게시글을 조회")
    public String read(
            @ShellOption(help = "조회할 게시글의 id를 입력합니다") String id
    ) {
        Optional<Board> boardOptional = boardRepository.read(Long.valueOf(id));
        if (boardOptional.isEmpty()){
            return """
                    게시글이 존재하지 않습니다.
                    """;
        }

        return """
                게시글을 조회하였습니다.
                %s""".formatted((boardToText(boardOptional.get())));
    }

    @ShellMethod(key = "list", prefix = "-", value = "모든 게시글 조회")
    public String list() {
        List<Board> boards = boardRepository.readAll();
        return """
                게시글을 조회하였습니다.
                %s""".formatted(boards.stream().map(this::boardToText).collect(Collectors.joining("\r\n--------------------------------------------\r\n")));
    }

    @ShellMethod(key = "update", prefix = "-", value = "id에 해당하는 게시글을 수정")
    public String update(
            @ShellOption(
                    value = {"id"},
                    help = "수정할 게시글의 id를 입력합니다."
            ) Long id,
            @ShellOption(
                    value = {"t", "title"},
                    help = "수정할 게시글의 제목을 입력합니다."
            ) String title,
            @ShellOption(
                    value = {"c", "content"},
                    help = "수정할 게시글의 내용을 입력합니다."
            ) String content

    ) {
        Board board = boardRepository.update(id, title, content);

        return """
                게시글을 수정하였습니다.
                %s""".formatted((boardToText(board)));
    }


    @ShellMethod(key = "delete", prefix = "-", value = "id에 해당하는 게시글을 삭제")
    public String delete(
            @ShellOption(help = "삭제할 게시글의 id를 입력합니다.") Long id
    ) {
        boardRepository.delete(id);

        return """
                게시글을 삭제하였습니다.
                """;
    }


    private String boardToText(Board board) {
        return """
                id : %d
                title : %s
                content : %s
                createdAt : %s
                updatedAt : %s""".formatted(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
