package com.foodify.server.modules.restaurants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "RestaurantAdminPayload", description = "Credentials for the restaurant administrator account")
public class NewUserDto {

    @NotBlank(message = "Administrator name is required")
    @Schema(description = "Full name of the restaurant administrator", example = "Ava Carter")
    private String name;

    @NotBlank(message = "Administrator email is required")
    @Email(message = "Administrator email must be valid")
    @Schema(description = "Email used for logging into the restaurant dashboard", example = "ava.carter@example.com")
    private String email;

    @NotBlank(message = "Administrator password is required")
    @Schema(description = "Raw password that will be encoded before saving", example = "Sup3rSecret!", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;
}
