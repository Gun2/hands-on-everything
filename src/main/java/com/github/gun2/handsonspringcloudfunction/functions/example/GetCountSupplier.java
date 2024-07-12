package com.github.gun2.handsonspringcloudfunction.functions.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component(value = "get-count")
@RequiredArgsConstructor
public class GetCountSupplier implements Supplier<Long> {
    private final CountComponent countComponent;

    @Override
    public Long get() {
        return countComponent.get();
    }
}
