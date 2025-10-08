package com.foodify.server.modules.restaurants.sync;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "catalog.cdc")
public class CatalogCdcProperties {

    @NotBlank
    private String menuItemTopic = "catalog-service.catalog.menu_items";

    @NotBlank
    private String menuItemExtraTopic = "catalog-service.catalog.menu_item_extras";

    @NotBlank
    private String restaurantTopic = "catalog-service.catalog.restaurants";
}
