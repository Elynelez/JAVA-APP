package com.miempresa.miapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.miempresa.miapp.model.Courier;
import com.miempresa.miapp.model.User;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findByUserId(User user);
}