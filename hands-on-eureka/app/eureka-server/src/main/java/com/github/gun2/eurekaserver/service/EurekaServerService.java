package com.github.gun2.eurekaserver.service;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.registry.InstanceRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EurekaServerService {
    private final InstanceRegistry instanceRegistry;

    public Applications getApplications(){
        Applications applications = instanceRegistry.getApplications();
        return applications;
    }
}
