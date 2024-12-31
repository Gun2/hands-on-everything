package com.github.gun2.server.websocket;

import lombok.Getter;

@Getter
public class ChangeDataEvent<T> {
    private final Type type;
    private final T data;

    public ChangeDataEvent(Type type, T data) {
        this.type = type;
        this.data = data;
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE;
    }
}
