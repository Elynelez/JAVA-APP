package com.miempresa.miapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miempresa.miapp.model.Client;
import com.miempresa.miapp.model.User;

public interface ClientRepository extends JpaRepository<Client, String> {
    List<Client> findByUserId(User user);
}