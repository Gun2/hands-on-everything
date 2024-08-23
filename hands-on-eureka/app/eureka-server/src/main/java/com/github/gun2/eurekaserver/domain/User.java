package com.github.gun2.eurekaserver.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Integer id;
    private String username;
    private String apiKey;

    public User(Integer id, String username, String apiKey) {
        this.id = id;
        this.username = username;
        this.apiKey = apiKey;
    }
}
