package com.miempresa.miapp.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miempresa.miapp.dto.PickupSummary;
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
public class PickupController {

        @Autowired
        private RouteRepository routeRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CourierRepository courierRepository;

        @Autowired
        private ClientRepository clientRepository;

        @GetMapping("/pickup/table")
        public String pickup_table(Model model) {
                List<Route> routes = routeRepository.findAll();

                Map<Integer, List<Route>> groupedRoutes = routes.stream()
                                .collect(Collectors.groupingBy(Route::getPickupRoute));

                Map<Integer, PickupSummary> grouped = groupedRoutes.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> {
                                                        List<Route> list = entry.getValue();
                                                        LocalDateTime earliest = list.stream()
                                                                        .map(Route::getCreatedAt)
                                                                        .min(LocalDateTime::compareTo)
                                                                        .orElse(LocalDateTime.now());

                                                        String courierName = list.stream()
                                                                        .map(r -> r.getPickupCourier() != null
                                                                                        ? r.getPickupCourier().getName()
                                                                                        : null)
                                                                        .filter(n -> n != null)
                                                                        .findFirst()
                                                                        .orElse("No asignado");

                                                        return new PickupSummary(earliest, courierName,
                                                                        String.valueOf(entry.getKey()),
                                                                        list.size());
                                                }));

                model.addAttribute("pickups", grouped.values());
                return "pages/pickup-table";
        }

        @GetMapping("/pickup/form")
        public String delivery_form(Model model) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();

                User usuario = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

                List<Courier> couriers = courierRepository.findByUserId(usuario);
                model.addAttribute("couriers", couriers);

                List<Client> clients = clientRepository.findByUserId(usuario);
                model.addAttribute("clients", clients);

                return "pages/pickup-form";
        }

        @PostMapping("/pickup/travel")
        public String saveRoute(@RequestParam List<String> orders,
                        @RequestParam String courier) {

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();

                // Buscar el usuario en la base de datos
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

                Long courierId = Long.parseLong(courier);
                Courier pickupCourier = courierRepository.findById(courierId)
                                .orElseThrow(() -> new RuntimeException("Courier no encontrado: " + courier));

                int pickupRouteId = (int) (System.currentTimeMillis() / 1000);

                for (String code : orders) {
                        Client client = clientRepository.findById(code)
                                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + code));

                        Route route = new Route();
                        route.setClientName(client.getName());
                        route.setAddress(client.getAddress());
                        route.setCreatedAt(LocalDateTime.now());
                        route.setDelivered(false);
                        route.setPickupCourier(pickupCourier);
                        route.setPickupRoute(pickupRouteId);
                        route.setClient(client);
                        route.setUserId(user);

                        routeRepository.save(route);
                }

                return "redirect:/delivery/pickup/table?success=true";
        }
}
