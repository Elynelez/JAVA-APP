package com.miempresa.miapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miempresa.miapp.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
