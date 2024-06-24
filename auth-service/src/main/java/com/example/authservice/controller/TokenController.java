package com.example.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import com.example.authservice.dto.UserDto;
import com.example.authservice.service.AuthService;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final AuthService authService;

    @Autowired
    public TokenController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token.")
    @PostMapping(value = "/generate")
    public ResponseEntity<?> generateToken(@RequestBody UserDto loginUser) throws AuthenticationException {
        return ResponseEntity.ok(authService.generateTokenForAuthUser(loginUser));
    }
}
