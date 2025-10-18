package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DriverShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverAvailabilityService {

    private final DriverShiftRepository driverShiftRepository;
    private final DriverLocationService driverLocationService;
    private final DriverDispatchService driverDispatchService;

    @Transactional
    public void refreshAvailability(Long driverId) {
        if (driverId == null) {
            return;
        }

        boolean hasActiveShift = driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE)
                .isPresent();

        if (hasActiveShift) {
            log.debug("Marking driver {} as available after order update", driverId);
            driverLocationService.markAvailable(String.valueOf(driverId));
            driverDispatchService.triggerSearchForPendingOrders();
        } else {
            log.debug("Marking driver {} as unavailable after order update", driverId);
            driverLocationService.markUnavailable(String.valueOf(driverId));
        }
    }
}
