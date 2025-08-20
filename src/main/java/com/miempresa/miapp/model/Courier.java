package com.miempresa.miapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "courier_java_app")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    public Courier() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public User getUserId() { return userId; }
    public void setUserId(User userId) { this.userId = userId; }
}
