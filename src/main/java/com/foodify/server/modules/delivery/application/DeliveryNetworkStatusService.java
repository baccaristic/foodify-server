package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.dto.DeliveryNetworkStatus;
import com.foodify.server.modules.delivery.dto.DeliveryNetworkStatusDto;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeliveryNetworkStatusService {

    private static final Set<OrderStatus> DRIVER_ASSIGNMENT_STATUSES = EnumSet.of(
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP
    );

    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;

    public DeliveryNetworkStatusDto getNetworkStatus() {
        long availableDrivers = driverRepository.countByAvailableTrue();
        long waitingForAssignment = orderRepository
                .countByStatusInAndDeliveryIsNullAndPendingDriverIsNullAndArchivedAtIsNull(DRIVER_ASSIGNMENT_STATUSES);
        long awaitingDriverResponse = orderRepository
                .countByStatusInAndDeliveryIsNullAndPendingDriverIsNotNullAndArchivedAtIsNull(DRIVER_ASSIGNMENT_STATUSES);

        DeliveryNetworkStatus status = resolveStatus(availableDrivers, waitingForAssignment, awaitingDriverResponse);
        String message = buildMessage(status);

        return new DeliveryNetworkStatusDto(
                status,
                message,
                availableDrivers,
                waitingForAssignment,
                awaitingDriverResponse
        );
    }

    private DeliveryNetworkStatus resolveStatus(long availableDrivers,
                                                long waitingForAssignment,
                                                long awaitingDriverResponse) {
        if (availableDrivers == 0) {
            return DeliveryNetworkStatus.NO_DRIVERS_AVAILABLE;
        }
        if (waitingForAssignment > 0 || awaitingDriverResponse > 0) {
            return DeliveryNetworkStatus.BUSY;
        }
        return DeliveryNetworkStatus.AVAILABLE;
    }

    private String buildMessage(DeliveryNetworkStatus status) {
        return switch (status) {
            case NO_DRIVERS_AVAILABLE -> "No delivery drivers are currently available. Please try again soon.";
            case BUSY -> "Deliveries are currently busy. Driver assignment may take longer than usual.";
            case AVAILABLE -> "Delivery drivers are available.";
        };
    }
}
