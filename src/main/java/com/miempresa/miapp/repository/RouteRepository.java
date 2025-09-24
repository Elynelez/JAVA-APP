package com.miempresa.miapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miempresa.miapp.model.Route;
import com.miempresa.miapp.model.User;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByUserId(User user);
}
