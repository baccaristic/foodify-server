package com.foodify.server.modules.notifications.dto;

public record DeviceRegisterRequest(
        String deviceToken,
        String platform,
        String deviceId,
        String appVersion
) {}