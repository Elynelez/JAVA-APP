package com.miempresa.miapp.controller;

import com.miempresa.miapp.model.User;
import com.miempresa.miapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public Optional<User> searchByUsername(@RequestParam String username) {
        return userService.searchByUsername(username);
    }
}
