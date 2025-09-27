package com.miempresa.miapp.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miempresa.miapp.model.Client;
import com.miempresa.miapp.model.Courier;
import com.miempresa.miapp.model.Route;
import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.ClientRepository;
import com.miempresa.miapp.repository.CourierRepository;
import com.miempresa.miapp.repository.RouteRepository;
import com.miempresa.miapp.repository.UserRepository;

@Controller
@RequestMapping("/delivery")
public class RouteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/route/table")
    public String delivery_table(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Buscar el usuario en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Route> routes = routeRepository.findByUserId(user);
        model.addAttribute("routes", routes);
        return "pages/delivery-table";
    }

    @GetMapping("/route/form/{id_courier}/{id_user}")
    public String delivery_form(@PathVariable Long id_courier,
            @PathVariable Long id_user,
            Model model) {

        User user = userRepository.findById(id_user)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Courier courier = courierRepository.findById(id_courier)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        Route route = new Route();
        route.setDeliveryCourier(String.valueOf(courier.getId()));

        model.addAttribute("route", route);
        model.addAttribute("clients", clientRepository.findByUserId(user));

        return "pages/delivery-form";
    }

    @PostMapping("/route/save")
    public String saveRoute(@RequestParam List<String> orders,
            @RequestParam("deliveryCourier") String courier) {

        int deliveryRouteId = (int) (System.currentTimeMillis() / 1000);

        for (String code : orders) {
            Client client = clientRepository.findById(code)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + code));

            Route route = new Route();
            route.setClient(client); // relación ManyToOne
            route.setClientName(client.getName());
            route.setAddress(client.getAddress());
            route.setDelivered(true);
            route.setDeliveryCourier(courier);
            route.setDeliveryRoute(deliveryRouteId);

            routeRepository.save(route);
        }

        return "redirect:/delivery/route/table?success=true";
    }

}
