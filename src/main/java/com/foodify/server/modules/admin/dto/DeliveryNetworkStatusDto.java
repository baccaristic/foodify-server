package com.foodify.server.modules.admin.dto;

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
