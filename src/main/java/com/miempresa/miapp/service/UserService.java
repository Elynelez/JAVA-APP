package com.miempresa.miapp.service;

import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> searchByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User guardar(User user) {
        return userRepository.save(user);
    }
}
