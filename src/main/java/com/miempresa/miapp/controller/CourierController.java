package com.miempresa.miapp.controller;

import com.miempresa.miapp.model.Courier;
import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.CourierRepository;
import com.miempresa.miapp.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class CourierController {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/courier/save")
    public String saveCourier(Courier courier) {
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Buscar el usuario en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // Asignar el usuario al courier
        courier.setUserId(user);

        // Guardar el courier
        courierRepository.save(courier);

        return "redirect:/delivery/courier/form?success=true";
    }

    @GetMapping("/delivery/courier/table")
    public String listCouriers(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Courier> couriers = courierRepository.findByUserId(user);

        String baseUrl = request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");

        for (Courier courier : couriers) {
            String link = baseUrl + "/delivery/route/form/" + courier.getId() + "/" + courier.getUserId().getId();
            courier.setLink(link);
        }

        model.addAttribute("user", user);
        model.addAttribute("couriers", couriers);

        return "pages/courier-table";
    }

    @GetMapping("/delivery/courier/form")
    public String courier_form(Model model) {
        model.addAttribute("messenger", new Courier());
        return "pages/courier-form";
    }
}
