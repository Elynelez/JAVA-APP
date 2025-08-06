package com.miempresa.miapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users_java_app")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String rol = "USUARIO";
    private Boolean status = true;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public Boolean isStatus() {
        return status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // Setters (opcionalmente puedes agregarlos si los necesitas)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
