package com.foodify.addressesservice.addresses.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.foodify.addressesservice.addresses.domain.AddressType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class SavedAddressResponse {
    private final UUID id;
    private final Long userId;
    private final AddressType type;
    private final String label;
    private final CoordinatesDto coordinates;
    private final String formattedAddress;
    private final String placeId;
    private final String entrancePreference;
    private final String entranceNotes;
    private final String directions;
    private final String notes;
    private final boolean primary;
    private final JsonNode typeDetails;
    private final JsonNode metadata;
    private final Instant createdAt;
    private final Instant updatedAt;
}
