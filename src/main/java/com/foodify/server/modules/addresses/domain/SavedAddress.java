package com.foodify.server.modules.addresses.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.foodify.server.modules.addresses.domain.converter.JsonNodeAttributeConverter;
import com.foodify.server.modules.identity.domain.User;
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
@ToString(exclude = {"user"})
public class SavedAddress {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
