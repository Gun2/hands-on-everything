package com.github.gun2.handsonspringcloudfunction.utils.processcommander;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;

/**
 * process command 유틸
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessCommander {

    /**
     * command 실행
     * @param commandType 명령 타입
     * @param command 명령어
     * @return
     */
    public static CommandResult exec(CommandType commandType, String command){
        StringBuilder output = new StringBuilder();
        boolean isSuccess = true;
        ProcessBuilder processBuilder = new ProcessBuilder();
        switch (commandType){
            case BASH -> {
                processBuilder.command("bash");
            }
            default -> new IllegalArgumentException("지원되지 않는 command type 입니다.");
        }
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
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command failed with exit code: ").append(exitCode).append("\n");
            }

        } catch (IOException | InterruptedException e) {
            output.append(e.getMessage());
            isSuccess = false;
        }

        return new CommandResult(isSuccess, output.toString());
    }

}
