package com.foodify.server.modules.restaurants.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SaveSpecialDayRequest(
        String name,
        LocalDate date,
        boolean open,
        LocalTime opensAt,
        LocalTime closesAt
) {
}
