package com.foodify.server.modules.orders.mapper;

import com.foodify.server.modules.addresses.domain.GeoCoordinates;
import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.SavedAddressSummaryDto;

public final class SavedAddressSummaryMapper {

    private SavedAddressSummaryMapper() {
    }

    public static SavedAddressSummaryDto from(SavedAddress savedAddress) {
        if (savedAddress == null) {
            return null;
        }

        return new SavedAddressSummaryDto(
                savedAddress.getId(),
                savedAddress.getType(),
                savedAddress.getLabel(),
                savedAddress.getFormattedAddress(),
                savedAddress.getPlaceId(),
                savedAddress.getEntrancePreference(),
                savedAddress.getEntranceNotes(),
                savedAddress.getDirections(),
                savedAddress.getNotes(),
                Boolean.TRUE.equals(savedAddress.getPrimary())
        );
    }

    public static LocationDto toLocation(SavedAddress savedAddress) {
        if (savedAddress == null) {
            return null;
        }
        GeoCoordinates coordinates = savedAddress.getCoordinates();
        if (coordinates == null || coordinates.getLatitude() == null || coordinates.getLongitude() == null) {
            return null;
        }
        return new LocationDto(
                coordinates.getLatitude().doubleValue(),
                coordinates.getLongitude().doubleValue()
        );
    }
}
