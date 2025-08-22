package com.miempresa.miapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miempresa.miapp.model.Client;
import com.miempresa.miapp.model.Courier;
import com.miempresa.miapp.model.Route;
import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.ClientRepository;
import com.miempresa.miapp.repository.CourierRepository;
import com.miempresa.miapp.repository.RouteRepository;
import com.miempresa.miapp.repository.UserRepository;

public class RouteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/delivery/route/table")
    public String delivery_table(Model model) {
        List<Route> routes = routeRepository.findAll();
        model.addAttribute("routes", routes);
        return "pages/delivery-table";
    }

    @GetMapping("delivery/route/form")
    public String delivery_form(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Courier> couriers = courierRepository.findByUserId(usuario);
        model.addAttribute("coursiers", couriers);

        List<Client> clients = clientRepository.findByUserId(usuario);
        model.addAttribute("clients", clients);

        return "pages/delivery-form";
    }

    @PostMapping("delivery/travel")
    public String saveRoute(@RequestParam List<String> orders,
            @RequestParam String coursier) {

        for (String code : orders) {
            Client client = clientRepository.findById(code)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + code));

            Route route = new Route();
            route.setClientName(client.getName());
            route.setAddress(client.getAddress());
            route.setCreatedAt(LocalDateTime.now());
            route.setDelivered(false);
            routeRepository.save(route);
        }

        return "redirect:/delivery/table?success=true";
    }
}
