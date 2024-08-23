package com.github.gun2.handsonspringcloudfunction.functions.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Component(value = "increase-count")
public class IncreaseCountConsumer implements Consumer<Integer> {
    private final CountComponent countComponent;
    @Override
    public void accept(Integer count) {
        countComponent.getAndAdd(count);
    }
}
