package com.foodify.server.modules.addresses.mapper;

import com.foodify.server.modules.addresses.domain.GeoCoordinates;
import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.addresses.dto.CoordinatesDto;
import com.foodify.server.modules.addresses.dto.SaveAddressRequest;
import com.foodify.server.modules.addresses.dto.SavedAddressResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SavedAddressMapper {

    public void updateEntityFromRequest(SavedAddress entity, SaveAddressRequest request) {
        entity.setType(request.getType());
        entity.setLabel(request.getLabel());
        entity.setFormattedAddress(request.getFormattedAddress());
        entity.setPlaceId(request.getPlaceId());
        entity.setEntrancePreference(request.getEntrancePreference());
        entity.setEntranceNotes(request.getEntranceNotes());
        entity.setDirections(request.getDirections());
        entity.setNotes(request.getNotes());
        if (request.getIsPrimary() != null) {
            entity.setPrimary(Boolean.TRUE.equals(request.getIsPrimary()));
        } else if (entity.getPrimary() == null) {
            entity.setPrimary(Boolean.FALSE);
        }
        entity.setTypeDetails(request.getTypeDetails());
        entity.setMetadata(request.getMetadata());

        if (request.getCoordinates() != null) {
            CoordinatesDto coordinatesDto = request.getCoordinates();
            GeoCoordinates coordinates = entity.getCoordinates();
            if (coordinates == null) {
                coordinates = new GeoCoordinates();
            }
            coordinates.setLatitude(normalizeCoordinate(coordinatesDto.getLatitude()));
            coordinates.setLongitude(normalizeCoordinate(coordinatesDto.getLongitude()));
            coordinates.setGeohash(coordinatesDto.getGeohash());
            entity.setCoordinates(coordinates);
        } else {
            entity.setCoordinates(null);
        }
    }

    public SavedAddressResponse toResponse(SavedAddress entity) {
        CoordinatesDto coordinatesDto = null;
        if (entity.getCoordinates() != null) {
            coordinatesDto = new CoordinatesDto();
            coordinatesDto.setLatitude(entity.getCoordinates().getLatitude());
            coordinatesDto.setLongitude(entity.getCoordinates().getLongitude());
            coordinatesDto.setGeohash(entity.getCoordinates().getGeohash());
        }

        return SavedAddressResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .type(entity.getType())
                .label(entity.getLabel())
                .coordinates(coordinatesDto)
                .formattedAddress(entity.getFormattedAddress())
                .placeId(entity.getPlaceId())
                .entrancePreference(entity.getEntrancePreference())
                .entranceNotes(entity.getEntranceNotes())
                .directions(entity.getDirections())
                .notes(entity.getNotes())
                .primary(Boolean.TRUE.equals(entity.getPrimary()))
                .typeDetails(entity.getTypeDetails())
                .metadata(entity.getMetadata())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private BigDecimal normalizeCoordinate(BigDecimal value) {
        return value == null ? null : value.setScale(6, RoundingMode.HALF_UP);
    }
}
