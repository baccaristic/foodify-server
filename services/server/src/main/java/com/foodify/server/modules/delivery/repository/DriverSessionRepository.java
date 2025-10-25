package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DriverSession;
import com.foodify.server.modules.delivery.domain.DriverSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DriverSessionRepository extends JpaRepository<DriverSession, Long> {

    List<DriverSession> findByDriverIdAndStatus(Long driverId, DriverSessionStatus status);

    Optional<DriverSession> findBySessionTokenAndStatus(String sessionToken, DriverSessionStatus status);

    List<DriverSession> findByStatusAndLastHeartbeatAtBefore(DriverSessionStatus status, Instant lastHeartbeatAt);
}
