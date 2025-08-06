package com.miempresa.miapp;

import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.UserRepository;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("delivery/table")
    public String table() {
        return "pages/table";
    }

    @GetMapping("delivery/form")
    public String delivery_form() {
        return "pages/delivery-form";
    }

    @GetMapping("login")
    public String login() {
        return "pages/login"; // Este es el nombre del archivo login.html o login.jsp
    }

    @GetMapping("register")
    public String registerForm() {
        return "pages/register";
    }

    @PostMapping("register")
    public String processRegister(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        // Validar si el usuario ya existe
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/pages/register?error=usuario_existente";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreationDate(LocalDateTime.now());
        user.setRol("USUARIO");
        user.setStatus(true);

        userRepository.save(user);

        return "redirect:/pages/login?registered=true";
    }
}
