package com.miempresa.miapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

// Importar tus controladores
import com.miempresa.miapp.controller.RegisterController;
import com.miempresa.miapp.controller.RouteController;
import com.miempresa.miapp.controller.ClientController;
import com.miempresa.miapp.controller.CourierController;

@Controller
public class AppController {

    // Inyectar los otros controladores
    @Autowired
    private RegisterController registerController;

    @Autowired
    private RouteController routeController;

    @Autowired
    private ClientController clientController;

    @Autowired
    private CourierController courierController;

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

}
