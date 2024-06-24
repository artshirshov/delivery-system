package com.example.authservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import com.example.authservice.service.UserServiceImpl;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return mock(PasswordEncoder.class);
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository(), passwordEncoder());
    }
}
