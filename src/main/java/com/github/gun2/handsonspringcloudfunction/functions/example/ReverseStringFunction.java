package com.github.gun2.handsonspringcloudfunction.functions.example;

import org.springframework.stereotype.Component;

import java.util.function.Function;

//bean 이름으로 url path를 지정할 수 있음
@Component(value = "reverse-string")
public class ReverseStringFunction implements Function<String, String> {
    @Override
    public String apply(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}
