package com.miempresa.miapp.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.UserRepository;

@Controller
@RequestMapping("/register")  
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String registerForm() {
        return "pages/register";  // Renderiza la vista
    }

    @PostMapping
    public String processRegister(@RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error=usuario_existente";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreationDate(LocalDateTime.now());
        user.setRol("USUARIO");
        user.setStatus(true);

        userRepository.save(user);

        return "redirect:/login?registered=true";
    }
}
