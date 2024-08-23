package com.github.gun2.eurekaserver.repository;

import com.github.gun2.eurekaserver.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class UserRepository {

    private final Map<Integer, User> store;
    public UserRepository() {
        this.store = Stream.of(
                new User(1, "user1", "API-KEY1"),
                new User(2, "user2", "API-KEY2"),
                new User(3, "user3", "API-KEY3"),
                new User(4, "user4", "API-KEY4"),
                new User(6, "user5", "API-KEY5")
        ).collect(Collectors.toConcurrentMap(
                user -> user.getId(),
                user -> user,
                (object, object2) -> object
        ));
    }

    public Optional<User> findById(Integer id){
        return Optional.ofNullable(store.get(id));
    }

    public Optional<User> findByApiKey(String apiKey){
        return store.values().stream().filter(
                user -> user.getApiKey().equals(apiKey)
        ).findAny();
    }
}
