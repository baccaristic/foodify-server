package com.foodify.server.modules.delivery.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@ConfigurationProperties(prefix = "driver.assignment")
public record DriverAssignmentProperties(
        @DefaultValue({"2","4","6","8","12","18","25"}) List<Double> searchRadiiKm,
        @DefaultValue("10") int candidateLimit,
        @DefaultValue("0.5") double distanceWeight,
        @DefaultValue("0.25") double idleWeight,
        @DefaultValue("0.15") double etaWeight,
        @DefaultValue("0.10") double capacityWeight,
        @DefaultValue("90") double maxIdleBonusMinutes,
        @DefaultValue("15") double targetEtaMinutes,
        @DefaultValue("0.9") double earlyExitScoreThreshold,
        @DefaultValue("1.5") double earlyExitDistanceKm,
        @DefaultValue("3") int maxConsecutiveDeclines,
        @DefaultValue("10") int declineBlockMinutes,
        @DefaultValue("0.25") double declinePenaltyWeight
) {
}
