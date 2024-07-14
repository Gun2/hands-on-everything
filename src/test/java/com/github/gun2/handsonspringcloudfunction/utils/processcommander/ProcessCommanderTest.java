package com.github.gun2.handsonspringcloudfunction.utils.processcommander;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessCommanderTest {

    @Test
    void shell_echo_hi() {
        /** given */
        String command = "echo hi";

        /** when */
        CommandResult echoHi = ProcessCommander.exec(CommandType.BASH, command);

        /** then */
        Assertions.assertTrue(echoHi.isSuccess());
        Assertions.assertTrue(echoHi.getOutput().contains("hi"));
    }

    @Test
    void shell_command_with_lien() {
        /** given */
        String command = """
                echo '#########################################'
                echo '#                 hihi                  #'
                echo '#########################################'
                """;
        /** when */
        CommandResult echoHi = ProcessCommander.exec(CommandType.BASH, command);

        /** then */
        Assertions.assertTrue(echoHi.isSuccess());
        Assertions.assertTrue(echoHi.getOutput().contains("hihi"));
    }

    @Test
    void cmd_echo_hi() {
        /** given */
        String command = """
@echo off
setlocal enabledelayedexpansion

rem 삼각형 높이 설정
set height=10

rem 각 줄을 출력
for /L %%i in (1,1,%height%) do (
    set line=
    for /L %%j in (1,1,%%i) do (
        set line=!line!*
    )
    echo !line!
)

endlocal
pause
                """;

        /** when */
        CommandResult echoHi = ProcessCommander.exec(CommandType.BATCH, command);

        /** then */
        Assertions.assertTrue(echoHi.isSuccess());
        Assertions.assertTrue(echoHi.getOutput().contains("**********"));
    }
}