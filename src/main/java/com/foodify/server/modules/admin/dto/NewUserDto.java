package com.foodify.server.modules.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "RestaurantAdminPayload", description = "Credentials for the restaurant administrator account")
public class NewUserDto {

    @Schema(description = "Full name of the restaurant administrator", example = "Ava Carter")
    private String name;

    @Schema(description = "Email used for logging into the restaurant dashboard", example = "ava.carter@example.com")
    private String email;

    @Schema(description = "Raw password that will be encoded before saving", example = "Sup3rSecret!", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;
}
