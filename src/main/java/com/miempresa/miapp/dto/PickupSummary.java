package com.miempresa.miapp.dto;

import java.time.LocalDateTime;

public class PickupSummary {

    private LocalDateTime createdAt;
    private String pickupCourierName;
    private String pickupRoute;
    private long packageCount;

    public PickupSummary(LocalDateTime createdAt, String pickupCourierName, String pickupRoute, long packageCount) {
        this.createdAt = createdAt;
        this.pickupCourierName = pickupCourierName;
        this.pickupRoute = pickupRoute;
        this.packageCount = packageCount;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPickupCourierName() { return pickupCourierName; }
    public String getPickupRoute() { return pickupRoute; }
    public long getPackageCount() { return packageCount; }
}
