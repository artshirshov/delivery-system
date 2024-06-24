package com.example.authservice.service;

import com.example.authservice.dto.UserDto;
import com.example.authservice.model.User;

public interface UserService {

    User createUser(UserDto userDto);
}
