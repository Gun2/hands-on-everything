package com.github.gun2.handsonspringcloudfunction.utils.processcommander;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * process command 유틸
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessCommander {

    /**
     * command 실행
     * @param commandType 명령 타입
     * @param command 명령어
     * @return
     */
    public static CommandResult exec(CommandType commandType, String command) {
        switch (commandType){
            case BASH -> {
                return execBash(command);
            }
            case BATCH -> {
                return execBatch(command);

            }
        }
        throw new IllegalArgumentException("지원되지 않는 command type 입니다.");
    }

    /**
     * bash 실행
     * @param command
     * @return
     */
    private static CommandResult execBash(String command) {
        StringBuilder output = new StringBuilder();
        boolean isSuccess = true;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash");
        try {
            Process process = processBuilder.start();
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(command);
                writer.flush();
            }
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader =
                    new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
                isSuccess = false;
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command failed with exit code: ").append(exitCode).append("\n");
            }

        } catch (IOException | InterruptedException e) {
            output.append(e.getMessage());
            isSuccess = false;
        }
        return new CommandResult(isSuccess, removeLastNewline(output.toString()));
    }

    private static CommandResult execBatch(String command){
        StringBuilder output = new StringBuilder();
        boolean isSuccess = true;
        ProcessBuilder processBuilder = new ProcessBuilder();
        File file = createFile(command);
        processBuilder.command("cmd.exe", "/c", file.getAbsolutePath());
        try {
            Process process = processBuilder.start();
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(command);
                writer.write("\nexit\n");
                writer.flush();
            }
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader =
                    new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
                isSuccess = false;
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command failed with exit code: ").append(exitCode).append("\n");
            }

        } catch (IOException | InterruptedException e) {
            output.append(e.getMessage());
            isSuccess = false;
        } finally {
            if (file != null && file.exists()){
                file.delete();
            }
        }
        return new CommandResult(isSuccess, removeLastNewline(output.toString()));
    }

    private static File createFile(String content){
        try {
            File scriptFile = File.createTempFile(UUID.randomUUID().toString(), ".bat");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(scriptFile, StandardCharsets.UTF_8))) {
                writer.write(isLf(content) ? convertLfToCrLf(content) : content);
            }
            return scriptFile;
        } catch (IOException e) {
            log.error("createFile : {}", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * LF (\\n) 방식의 문자열을 CRLF (\\r\\n) 방식으로 변환하는 메서드
     *
     * @param input LF 방식의 문자열
     * @return CRLF 방식의 문자열
     */
    private static String convertLfToCrLf(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\n", "\r\n");
    }

    /**
     * 문자 마지막에 개행 문자가 존재할 경우 제거
     * @param input
     */
    public static String removeLastNewline(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        if (input.endsWith("\r\n")) {
            return input.substring(0, input.length() - 2);
        }
        if (input.endsWith("\n")) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }

    private static boolean isLf(String input) {
        if (input == null) {
            return false;
        }
        // 문자열에 LF가 있지만 CRLF는 아닌 경우
        return input.contains("\n") && !input.contains("\r\n");
    }

}
