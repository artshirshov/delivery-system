package com.example.authservice.service;

import com.example.authservice.config.TestConfig;
import com.example.authservice.dto.UserDto;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUserSuccessTest() {
        UserDto userDto = new UserDto();
        userDto.setName("Test name");
        userDto.setPassword("Test password");

        User user = new User();
        user.setId(1L);
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        assertDoesNotThrow(() -> userService.createUser(userDto));
    }
}