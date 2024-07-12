package com.github.gun2.handsonspringcloudfunction.utils.processcommander;

import lombok.Getter;
import lombok.ToString;

/**
 * 명령어 실행 결과
 */
@Getter
@ToString
public class CommandResult {
    /**
     * 명령어 성공 유무
     */
    private final boolean isSuccess;
    /**
     * 출력 결과
     */
    private final String output;

    public CommandResult(boolean isSuccess, String output) {
        this.isSuccess = isSuccess;
        this.output = output;
    }
}
