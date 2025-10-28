package com.foodify.server.modules.delivery.dto;

public record DeliveryNetworkStatusDto(
        DeliveryNetworkStatus status,
        String message,
        long availableDrivers,
        long waitingForAssignment,
        long awaitingDriverResponse
) {
    public long pendingOrders() {
        return waitingForAssignment + awaitingDriverResponse;
    }
}
