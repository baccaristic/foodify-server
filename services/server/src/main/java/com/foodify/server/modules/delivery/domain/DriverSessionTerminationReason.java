package com.foodify.server.modules.delivery.domain;

public enum DriverSessionTerminationReason {
    REPLACED_BY_NEW_LOGIN,
    HEARTBEAT_TIMEOUT,
    LOGOUT
}
