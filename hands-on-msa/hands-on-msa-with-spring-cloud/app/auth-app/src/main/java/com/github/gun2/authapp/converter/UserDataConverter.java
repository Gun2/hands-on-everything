package com.github.gun2.authapp.converter;

import com.github.gun2.authapp.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDataConverter {

    public static UserDetails entityToUserDetails(User user){
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()).build();
    }
}
