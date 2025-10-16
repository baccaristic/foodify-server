package com.foodify.server.modules.delivery.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "driver.session")
@Getter
@Setter
@Validated
public class DriverSessionSettings {

    /**
     * Number of seconds before a session is considered stale due to missing heartbeat.
     */
    private long heartbeatTimeoutSeconds = 120;

    /**
     * Interval (ms) at which stale sessions should be checked.
     */
    private long heartbeatCheckIntervalMs = 30000;
}
