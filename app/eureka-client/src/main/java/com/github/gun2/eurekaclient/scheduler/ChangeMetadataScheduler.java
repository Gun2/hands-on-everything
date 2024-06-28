package com.github.gun2.eurekaclient.scheduler;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeMetadataScheduler {
    private final AtomicLong atomicLong = new AtomicLong();
    private final ApplicationInfoManager applicationInfoManager;
    @Scheduled(fixedDelay = 1000)
    public void changeMetadata(){
        InstanceInfo info = applicationInfoManager.getInfo();
        Map<String, String> metadataMap = info.getMetadata();
        metadataMap.put("count", String.valueOf(atomicLong.getAndIncrement()));
        applicationInfoManager.registerAppMetadata(metadataMap);
    }

}
