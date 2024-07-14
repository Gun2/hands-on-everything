package com.github.gun2.handsonspringcloudfunction.functions.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gun2.handsonspringcloudfunction.utils.processcommander.CommandType;
import com.github.gun2.handsonspringcloudfunction.utils.processcommander.ProcessCommander;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * bash shell 원격 실행
 * curl http://localhost:8080/remote-batch -H "Content-Type: text/plain" -d 'echo test'
 */
@RequiredArgsConstructor
@Component("remote-batch")
public class RemoteBatchFunction implements Function<String, String> {
    private final ObjectMapper objectMapper;
    @Override
    public String apply(String script) {
        try {
            return objectMapper.writeValueAsString(ProcessCommander.exec(CommandType.BATCH, script));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
