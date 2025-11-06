package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DriverShiftRepository;
import com.foodify.server.modules.identity.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverAvailabilityService {

    private final DriverShiftRepository driverShiftRepository;
    private final DriverLocationService driverLocationService;
    private final DriverDispatchService driverDispatchService;
    private final DriverRepository driverRepository;
    private final DriverFinancialService driverFinancialService;

    @Transactional
    public void refreshAvailability(Long driverId) {
        if (driverId == null) {
            return;
        }

        boolean hasActiveShift = driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE)
                .isPresent();

        if (hasActiveShift) {
            boolean canWork = driverRepository.findById(driverId)
                    .map(driver -> {
                        boolean depositRequired = driverFinancialService.isDepositRequired(driver);
                        boolean deadlinePassed = driverFinancialService.hasDepositDeadlinePassed(driver);
                        
                        // Driver cannot work if deposit is required AND deadline has passed
                        return !(depositRequired && deadlinePassed);
                    })
                    .orElse(false);
                    
            if (!canWork) {
                log.debug("Keeping driver {} unavailable due to deposit deadline passed", driverId);
                driverLocationService.markUnavailable(String.valueOf(driverId));
                return;
            }
            log.debug("Marking driver {} as available after order update", driverId);
            driverLocationService.markAvailable(String.valueOf(driverId));
            driverDispatchService.triggerSearchForPendingOrders();
        } else {
            log.debug("Marking driver {} as unavailable after order update", driverId);
            driverLocationService.markUnavailable(String.valueOf(driverId));
        }
    }

    @EventListener
    public void handleDriverDepositConfirmed(DriverDepositConfirmedEvent event) {
        if (event == null || event.driverId() == null) {
            return;
        }
        refreshAvailability(event.driverId());
    }
}
