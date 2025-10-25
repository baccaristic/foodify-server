package com.foodify.server.modules.orders.dto;

import com.foodify.server.modules.addresses.domain.AddressType;

import java.util.UUID;

public record SavedAddressSummaryDto(
        UUID id,
        AddressType type,
        String label,
        String formattedAddress,
        String placeId,
        String entrancePreference,
        String entranceNotes,
        String directions,
        String notes,
        boolean primary
) {}
