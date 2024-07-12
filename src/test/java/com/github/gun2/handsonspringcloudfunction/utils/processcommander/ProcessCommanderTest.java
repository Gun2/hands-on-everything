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
}