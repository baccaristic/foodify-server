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
    
    // Multi-language support
    @Schema(description = "Restaurant name in English", example = "La Piazza")
    private String nameEn;
    @Schema(description = "Restaurant name in French", example = "La Piazza")
    private String nameFr;
    @Schema(description = "Restaurant name in Arabic", example = "لا بيازا")
    private String nameAr;

    @NotBlank
    @Schema(description = "Street address of the restaurant", example = "742 Evergreen Terrace")
    private String address;

    @NotBlank
    @Schema(description = "Primary phone contact", example = "+1 202 555 0136")
    private String phone;

    @Schema(description = "Short description visible to customers", example = "Authentic Italian cuisine with a modern twist")
    private String description;
    
    // Multi-language support for description
    @Schema(description = "Restaurant description in English")
    private String descriptionEn;
    @Schema(description = "Restaurant description in French")
    private String descriptionFr;
    @Schema(description = "Restaurant description in Arabic")
    private String descriptionAr;

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

    @Schema(description = "Commission rate applied to the restaurant's orders (driver + platform share)", example = "0.1700")
    private BigDecimal commissionRate;

    @Valid
    @Schema(description = "Optional admin account information. Required when creating a restaurant")
    private NewUserDto admin;
}
