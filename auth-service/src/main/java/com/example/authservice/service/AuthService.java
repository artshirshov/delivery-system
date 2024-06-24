package com.example.authservice.service;

import org.springframework.security.core.AuthenticationException;
import com.example.authservice.dto.UserDto;

public interface AuthService {

    String generateTokenForAuthUser(UserDto loginUser) throws AuthenticationException;
}
