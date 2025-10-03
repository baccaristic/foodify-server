package com.foodify.server.modules.restaurants.dto;

import java.util.List;

public record RestaurantDetailsResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        String address,
        String phone,
        String type,
        String rating,
        String openingHours,
        String closingHours,
        double latitude,
        double longitude,
        List<RestaurantBadge> highlights,
        List<String> quickFilters,
        List<MenuItemSummary> topSales,
        List<MenuCategory> categories
) {
    public record RestaurantBadge(String label, String value) {}

    public record MenuItemSummary(
            Long id,
            String name,
            String description,
            double price,
            String imageUrl,
            boolean popular,
            List<String> tags
    ) {}

    public record MenuCategory(String name, List<MenuItemDetails> items) {}

    public record MenuItemDetails(
            Long id,
            String name,
            String description,
            double price,
            String imageUrl,
            boolean popular,
            List<String> tags,
            List<MenuOptionGroupDto> optionGroups
    ) {}

    public record MenuOptionGroupDto(
            Long id,
            String name,
            int minSelect,
            int maxSelect,
            boolean required,
            List<MenuItemExtraDto> extras
    ) {}

    public record MenuItemExtraDto(Long id, String name, double price, boolean defaultOption) {}
}
