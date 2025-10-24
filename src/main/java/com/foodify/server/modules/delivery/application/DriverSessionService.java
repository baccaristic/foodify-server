package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.DriverSession;
import com.foodify.server.modules.delivery.domain.DriverSessionStatus;
import com.foodify.server.modules.delivery.domain.DriverSessionTerminationReason;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DriverSessionRepository;
import com.foodify.server.modules.identity.domain.Driver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverSessionService {

    private final DriverSessionRepository driverSessionRepository;
    private final DriverLocationService driverLocationService;
    private final DriverSessionSettings driverSessionSettings;

    @Transactional
    public DriverSession startSession(Driver driver, String deviceId) {
        List<DriverSession> activeSessions = driverSessionRepository
                .findByDriverIdAndStatus(driver.getId(), DriverSessionStatus.ACTIVE);

        for (DriverSession session : activeSessions) {
            endSession(session, DriverSessionTerminationReason.REPLACED_BY_NEW_LOGIN);
        }

        DriverSession session = new DriverSession();
        session.setDriver(driver);
        session.setDeviceId(deviceId);
        session.setSessionToken(UUID.randomUUID().toString());
        session.setStatus(DriverSessionStatus.ACTIVE);
        session.setStartedAt(Instant.now());
        session.setLastHeartbeatAt(Instant.now());

        DriverSession saved = driverSessionRepository.save(session);
        driverLocationService.markAvailable(String.valueOf(driver.getId()));
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<DriverSession> findActiveSession(Long driverId) {
        return driverSessionRepository
                .findByDriverIdAndStatus(driverId, DriverSessionStatus.ACTIVE)
                .stream()
                .findFirst();
    }

    @Transactional(readOnly = true)
    public Optional<DriverSession> findActiveByToken(String sessionToken) {
        return driverSessionRepository.findBySessionTokenAndStatus(sessionToken, DriverSessionStatus.ACTIVE);
    }

    @Transactional
    public void recordHeartbeat(DriverSession session) {
        session.setLastHeartbeatAt(Instant.now());
        driverSessionRepository.save(session);
    }

    @Transactional
    public void endSession(DriverSession session, DriverSessionTerminationReason reason) {
        if (session.getStatus() == DriverSessionStatus.ENDED) {
            return;
        }

        session.setStatus(DriverSessionStatus.ENDED);
        session.setEndReason(reason);
        session.setEndedAt(Instant.now());
        driverSessionRepository.save(session);

        driverLocationService.markUnavailable(String.valueOf(session.getDriver().getId()));
    }

    @Transactional
    public void endSessionByToken(String sessionToken, DriverSessionTerminationReason reason) {
        driverSessionRepository
                .findBySessionTokenAndStatus(sessionToken, DriverSessionStatus.ACTIVE)
                .ifPresent(session -> endSession(session, reason));
    }

    @Scheduled(fixedDelayString = "${driver.session.heartbeat-check-interval-ms:30000}")
    @Transactional
    public void expireInactiveSessions() {
        Instant cutoff = Instant.now()
                .minus(Duration.ofSeconds(driverSessionSettings.getHeartbeatTimeoutSeconds()));
        List<DriverSession> expired = driverSessionRepository
                .findByStatusAndLastHeartbeatAtBefore(DriverSessionStatus.ACTIVE, cutoff);

        if (expired.isEmpty()) {
            return;
        }

        log.info("Expiring {} driver sessions after missed heartbeat", expired.size());
        expired.forEach(session -> endSession(session, DriverSessionTerminationReason.HEARTBEAT_TIMEOUT));
    }
}
