package com.foodify.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "orders.view")
public class OrderViewProperties {

    private int restaurantSnapshotLimit = 100;
    private int pendingDriverLimit = 25;

    public int getRestaurantSnapshotLimit() {
        return restaurantSnapshotLimit;
    }

    public void setRestaurantSnapshotLimit(int restaurantSnapshotLimit) {
        this.restaurantSnapshotLimit = restaurantSnapshotLimit;
    }

    public int getPendingDriverLimit() {
        return pendingDriverLimit;
    }

    public void setPendingDriverLimit(int pendingDriverLimit) {
        this.pendingDriverLimit = pendingDriverLimit;
    }
}
