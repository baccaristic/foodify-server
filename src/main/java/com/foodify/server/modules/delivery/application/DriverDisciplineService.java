package com.foodify.server.modules.delivery.application;
import com.foodify.server.modules.delivery.config.DriverAssignmentProperties;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverDisciplineService {

    private final DriverRepository driverRepository;
    private final DriverAssignmentProperties properties;

    @Transactional
    public void recordOffer(Long driverId) {
        if (driverId == null) {
            return;
        }
        driverRepository.findById(driverId).ifPresentOrElse(driver -> {
            driver.setTotalOrderOffers(increment(driver.getTotalOrderOffers()));
            clearExpiredBlock(driver);
            driverRepository.save(driver);
        }, () -> log.debug("Unable to record offer for missing driver {}", driverId));
    }

    @Transactional
    public void recordDecline(Long driverId) {
        if (driverId == null) {
            return;
        }
        driverRepository.findById(driverId).ifPresentOrElse(driver -> {
            driver.setTotalDeclines(increment(driver.getTotalDeclines()));
            driver.setConsecutiveDeclines(increment(driver.getConsecutiveDeclines()));
            if (driver.getConsecutiveDeclines() >= properties.maxConsecutiveDeclines()) {
                LocalDateTime blockedUntil = LocalDateTime.now().plusMinutes(properties.declineBlockMinutes());
                driver.setDeclineBlockedUntil(blockedUntil);
                driver.setConsecutiveDeclines(0);
                log.info("Temporarily blocked driver {} until {} after {} consecutive declines",
                        driverId, blockedUntil, properties.maxConsecutiveDeclines());
            }
            driverRepository.save(driver);
        }, () -> log.debug("Unable to record decline for missing driver {}", driverId));
    }

    @Transactional
    public void resetConsecutiveDeclines(Long driverId) {
        if (driverId == null) {
            return;
        }
        driverRepository.findById(driverId).ifPresent(driver -> {
            boolean changed = false;
            if (driver.getConsecutiveDeclines() != null && driver.getConsecutiveDeclines() > 0) {
                driver.setConsecutiveDeclines(0);
                changed = true;
            }
            if (clearExpiredBlock(driver)) {
                changed = true;
            }
            if (changed) {
                driverRepository.save(driver);
            }
        });
    }

    public boolean isTemporarilyBlocked(Driver driver) {
        if (driver == null) {
            return false;
        }
        LocalDateTime blockedUntil = driver.getDeclineBlockedUntil();
        return blockedUntil != null && blockedUntil.isAfter(LocalDateTime.now());
    }

    private boolean clearExpiredBlock(Driver driver) {
        LocalDateTime blockedUntil = driver.getDeclineBlockedUntil();
        if (blockedUntil != null && blockedUntil.isBefore(LocalDateTime.now())) {
            driver.setDeclineBlockedUntil(null);
            return true;
        }
        return false;
    }

    private int increment(Integer value) {
        if (value == null || value < 0) {
            return 1;
        }
        if (value >= Integer.MAX_VALUE - 1) {
            return Integer.MAX_VALUE;
        }
        return value + 1;
    }
}
