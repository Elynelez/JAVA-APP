package com.miempresa.miapp.controller;

import com.miempresa.miapp.model.Client;
import com.miempresa.miapp.model.Route;
import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.ClientRepository;
import com.miempresa.miapp.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute Client client) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        client.setUserId(usuario);
        clientRepository.save(client);

        return "redirect:/delivery/client/form?success=true";
    }

    @GetMapping("/check-client/{id}")
    public ResponseEntity<Boolean> checkClient(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        boolean exists = clientRepository.existsByIdAndUserId(id, usuario);

        return ResponseEntity.ok(exists);
    }

    @GetMapping("/delivery/client/table")
    public String client_table(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "pages/client-table";
    }

}
