package com.github.gun2.handsonspringshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class HandsOnSpringShellApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandsOnSpringShellApplication.class, args);
    }

}
