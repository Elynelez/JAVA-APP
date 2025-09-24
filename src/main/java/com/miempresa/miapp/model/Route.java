package com.miempresa.miapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "route_java_app")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User userId;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean delivered = false;

    // --- Nuevos campos ---
    @ManyToOne
    @JoinColumn(name = "pickup_courier", referencedColumnName = "id")
    private Courier pickupCourier;

    @Column(name = "pickup_route")
    private Integer pickupRoute;

    @Column(name = "delivery_courier")
    private String deliveryCourier;

    @Column(name = "delivery_route")
    private Integer deliveryRoute;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    // --- Constructores ---
    public Route() {
    }

    // --- Getters y Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Courier getPickupCourier() {
        return pickupCourier;
    }

    public void setPickupCourier(Courier pickupCourier) {
        this.pickupCourier = pickupCourier;
    }

    public Integer getPickupRoute() {
        return pickupRoute;
    }

    public void setPickupRoute(Integer pickupRoute) {
        this.pickupRoute = pickupRoute;
    }

    public String getDeliveryCourier() {
        return deliveryCourier;
    }

    public void setDeliveryCourier(String deliveryCourier) {
        this.deliveryCourier = deliveryCourier;
    }

    public Integer getDeliveryRoute() {
        return deliveryRoute;
    }

    public void setDeliveryRoute(Integer deliveryRoute) {
        this.deliveryRoute = deliveryRoute;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // --- equals y hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Route))
            return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                ", delivered=" + delivered +
                ", pickupCourier=" + pickupCourier +
                ", pickupRoute=" + pickupRoute +
                ", deliveryCourier='" + deliveryCourier + '\'' +
                ", deliveryRoute=" + deliveryRoute +
                '}';
    }
}
