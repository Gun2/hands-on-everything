package com.github.gun2.eurekaserver.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EurekaListener {

    /**
     * 새로운 인스턴스 등록 시 발생
     * @param event
     */
    @EventListener
    public void handleEurekaInstanceRegisteredEvent(EurekaInstanceRegisteredEvent event) {
        log.info("EurekaInstanceRegisteredEvent : {}", event);
    }

    /**
     * 등록된 인스턴스가 갱신 시 발생
     * @param event
     */
    @EventListener
    public void handleEurekaInstanceRenewedEvent(EurekaInstanceRenewedEvent event){
        log.info("EurekaInstanceRenewedEvent : {}", event);
    }

    /**
     * 인스턴스가 유레카 서버에서 취소될 때 발생
     * @param event
     */
    @EventListener
    public void handleEurekaInstanceCanceledEvent(EurekaInstanceCanceledEvent event){
        log.info("EurekaInstanceCanceledEvent : {}", event);
    }

    /**
     * eureka 서버의 레지스트리가 사용 가능할 때 발생
     * (레지스트리가 사용 가능해졌을 때마다 발생)
     * @param event
     */
    @EventListener
    public void handleEurekaRegistryAvailableEvent(EurekaRegistryAvailableEvent event) {
        log.info("EurekaRegistryAvailableEvent : {}", event);
    }

    /**
     * eureka 서버가 시작될 때 발생
     * (Eureka 서버가 완전히 초기화되고 실행될 때 한 번 발생)
     * @param event
     */
    @EventListener
    public void handleEurekaServerStartedEvent(EurekaServerStartedEvent event){
        log.info("EurekaServerStartedEvent : {}", event);

    }
}
