package com.github.gun2.authapp;

import com.github.gun2.authapp.entity.User;
import com.github.gun2.authapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner addSampleUsers(UserRepository userRepository, PasswordEncoder passwordEncoder){
		return args -> {
			userRepository.save(
					User.builder()
							.username("user1")
							.password(passwordEncoder.encode("password1"))
							.role("USER")
							.build()
			);
			userRepository.save(
					User.builder()
							.username("user2")
							.password(passwordEncoder.encode("password2"))
							.role("USER")
							.build()
			);
			userRepository.save(
					User.builder()
							.username("user3")
							.password(passwordEncoder.encode("password3"))
							.role("USER")
							.build()
			);
		};
	}
}
