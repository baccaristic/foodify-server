package com.foodify.server.modules.restaurants.dto;

import com.foodify.server.modules.restaurants.domain.RestaurantCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Schema(name = "AdminRestaurantRequest", description = "Details required to create or update a restaurant from the admin portal")
public class RestaurantDto {

    @NotBlank
    @Schema(description = "Restaurant display name", example = "La Piazza")
    private String name;

    @NotBlank
    @Schema(description = "Street address of the restaurant", example = "742 Evergreen Terrace")
    private String address;

    @NotBlank
    @Schema(description = "Primary phone contact", example = "+1 202 555 0136")
    private String phone;

    @Schema(description = "Short description visible to customers", example = "Authentic Italian cuisine with a modern twist")
    private String description;

    @Schema(description = "Restaurant categories", example = "[\"ITALIAN\", \"PASTA\"]")
    private Set<RestaurantCategory> categories;

    @Schema(description = "Latitude coordinate for delivery and pickup", example = "40.741895")
    private double latitude;

    @Schema(description = "Longitude coordinate for delivery and pickup", example = "-73.989308")
    private double longitude;

    @Schema(description = "Business license number", example = "LIC-2023-0001")
    private String licenseNumber;

    @Schema(description = "Tax identification number", example = "TIN-52-1234567")
    private String taxId;

    @Schema(description = "Share of each order allocated to the restaurant", example = "0.8800")
    private BigDecimal restaurantShareRate;

    @Valid
    @Schema(description = "Optional admin account information. Required when creating a restaurant")
    private NewUserDto admin;
}
