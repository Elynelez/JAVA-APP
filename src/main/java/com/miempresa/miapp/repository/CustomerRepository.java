package com.miempresa.miapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miempresa.miapp.model.Customer;
import com.miempresa.miapp.model.User;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByUserId(User user);

    Optional<Customer> findById(Long id);
}