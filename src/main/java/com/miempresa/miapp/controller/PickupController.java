package com.miempresa.miapp.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miempresa.miapp.dto.PickupSummary;
import com.miempresa.miapp.model.Route;
import com.miempresa.miapp.repository.RouteRepository;

@Controller
@RequestMapping("/delivery")
public class PickupController {

    @Autowired
    private RouteRepository routeRepository;

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
                                    .map(r -> r.getPickupCourier() != null ? r.getPickupCourier().getName() : null)
                                    .filter(n -> n != null)
                                    .findFirst()
                                    .orElse("No asignado");

                            return new PickupSummary(earliest, courierName, String.valueOf(entry.getKey()),
                                    list.size());
                        }));

        model.addAttribute("pickups", grouped.values());
        return "pages/pickup-table";
    }
}
