package org.example.controller;

import org.example.model.RegisterUserRequest;
import org.example.model.User;
import org.example.service.UserService;

import java.util.Objects;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

    public User register(RegisterUserRequest request) {
        return userService.register(request);
    }
}