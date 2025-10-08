package com.foodify.addressesservice.addresses.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.foodify.addressesservice.addresses.domain.converter.JsonNodeAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "saved_addresses")
@Getter
@Setter
@ToString
public class SavedAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 32)
    private AddressType type;

    @Column(name = "label")
    private String label;

    @Embedded
    private GeoCoordinates coordinates;

    @Column(name = "formatted_address", nullable = false)
    private String formattedAddress;

    @Column(name = "place_id")
    private String placeId;

    @Column(name = "entrance_preference")
    private String entrancePreference;

    @Column(name = "entrance_notes", columnDefinition = "TEXT")
    private String entranceNotes;

    @Column(name = "directions", columnDefinition = "TEXT")
    private String directions;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_primary")
    private Boolean primary = Boolean.FALSE;

    @Convert(converter = JsonNodeAttributeConverter.class)
    @Column(name = "type_details", columnDefinition = "TEXT")
    private JsonNode typeDetails;

    @Convert(converter = JsonNodeAttributeConverter.class)
    @Column(name = "metadata", columnDefinition = "TEXT")
    private JsonNode metadata;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
