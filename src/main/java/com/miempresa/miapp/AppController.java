package com.miempresa.miapp;

import com.miempresa.miapp.model.User;
import com.miempresa.miapp.model.Route;
import com.miempresa.miapp.model.Client;
import com.miempresa.miapp.model.Courier;
import com.miempresa.miapp.repository.UserRepository;
import com.miempresa.miapp.repository.CourierRepository;
import com.miempresa.miapp.repository.RouteRepository;
import com.miempresa.miapp.repository.ClientRepository;

import java.time.LocalDateTime;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("register")
    public String registerForm() {
        return "pages/register";
    }

    @PostMapping("register")
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
