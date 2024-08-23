package com.github.gun2.handsonspringcloudfunction.functions.example;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class CountComponent extends AtomicLong {
}
