package com.foodify.catalogservice.controller;

import com.foodify.catalogservice.api.CatalogMenuItemDto;
import com.foodify.catalogservice.api.CatalogMenuItemExtraDto;
import com.foodify.catalogservice.api.CatalogRestaurantDto;
import com.foodify.catalogservice.api.ExtrasLookupRequest;
import com.foodify.catalogservice.domain.MenuItem;
import com.foodify.catalogservice.domain.MenuItemExtra;
import com.foodify.catalogservice.domain.MenuOptionGroup;
import com.foodify.catalogservice.domain.Restaurant;
import com.foodify.catalogservice.service.CatalogApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogApplicationService catalogApplicationService;

    @GetMapping("/restaurants/{restaurantId}")
    @Transactional(readOnly = true)
    public CatalogRestaurantDto getRestaurant(@PathVariable Long restaurantId) {
        Restaurant restaurant = catalogApplicationService.getRestaurantOrThrow(restaurantId);
        return new CatalogRestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getType(),
                restaurant.getRating(),
                restaurant.getOpeningHours(),
                restaurant.getClosingHours(),
                restaurant.getDescription(),
                restaurant.getImageUrl(),
                restaurant.getTopChoice(),
                restaurant.getFreeDelivery(),
                restaurant.getTopEat(),
                restaurant.getDeliveryFee(),
                restaurant.getDeliveryTimeRange(),
                restaurant.getMenuItems().stream()
                        .map(menuItem -> new CatalogRestaurantDto.CatalogMenuItemSummary(menuItem.getId()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/menu-items/{menuItemId}")
    @Transactional(readOnly = true)
    public CatalogMenuItemDto getMenuItem(@PathVariable Long menuItemId) {
        MenuItem menuItem = catalogApplicationService.getMenuItemOrThrow(menuItemId);
        List<CatalogMenuItemExtraDto> extras = menuItem.getOptionGroups().stream()
                .map(MenuOptionGroup::getExtras)
                .flatMap(List::stream)
                .map(extra -> new CatalogMenuItemExtraDto(
                        extra.getId(),
                        extra.getOptionGroup() != null ? extra.getOptionGroup().getId() : null,
                        menuItem.getId(),
                        extra.getName(),
                        extra.getPrice(),
                        extra.isDefault()
                ))
                .toList();

        return new CatalogMenuItemDto(
                menuItem.getId(),
                menuItem.getRestaurant() != null ? menuItem.getRestaurant().getId() : null,
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getCategory(),
                menuItem.isPopular(),
                menuItem.getPrice(),
                menuItem.getPromotionLabel(),
                menuItem.getPromotionPrice(),
                menuItem.getPromotionActive(),
                menuItem.getImageUrls(),
                extras
        );
    }

    @PostMapping("/menu-item-extras/_lookup")
    @Transactional(readOnly = true)
    public List<CatalogMenuItemExtraDto> lookupExtras(@Valid @RequestBody ExtrasLookupRequest request) {
        List<MenuItemExtra> extras = catalogApplicationService.getExtras(request.ids());
        return extras.stream()
                .map(extra -> new CatalogMenuItemExtraDto(
                        extra.getId(),
                        extra.getOptionGroup() != null ? extra.getOptionGroup().getId() : null,
                        extra.getOptionGroup() != null && extra.getOptionGroup().getMenuItem() != null
                                ? extra.getOptionGroup().getMenuItem().getId() : null,
                        extra.getName(),
                        extra.getPrice(),
                        extra.isDefault()
                ))
                .toList();
    }
}
