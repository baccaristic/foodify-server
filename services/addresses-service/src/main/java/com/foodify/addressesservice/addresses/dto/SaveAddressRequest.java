package com.foodify.addressesservice.addresses.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.foodify.addressesservice.addresses.domain.AddressType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveAddressRequest {

    private String userId;

    @NotNull
    private AddressType type;

    private String label;

    @NotNull
    @Valid
    private CoordinatesDto coordinates;

    @NotBlank
    private String formattedAddress;

    private String placeId;

    private String entrancePreference;

    private String entranceNotes;

    private String directions;

    private String notes;

    private Boolean isPrimary;

    private JsonNode typeDetails;

    private JsonNode metadata;
}
