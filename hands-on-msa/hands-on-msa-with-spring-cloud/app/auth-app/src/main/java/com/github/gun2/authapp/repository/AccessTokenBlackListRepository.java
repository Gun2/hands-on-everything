package com.github.gun2.authapp.repository;

import com.github.gun2.authapp.entity.AccessTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenBlackListRepository extends JpaRepository<AccessTokenBlackList, Long> {
    Optional<AccessTokenBlackList> findByAccessTokenHash(String accessTokenHash);
}
