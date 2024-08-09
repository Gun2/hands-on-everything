package com.github.gun2.handsonspringshell.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class UtilCommand {
    private static final String GROUP = "Util Command";

    @ShellMethod(key = "cat", prefix = "-", value = "파일 일기", group = GROUP)
    public String cat(
            @ShellOption(
                    valueProvider = FileValueProvider.class
            ) File file
    ) throws IOException {
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }


}
