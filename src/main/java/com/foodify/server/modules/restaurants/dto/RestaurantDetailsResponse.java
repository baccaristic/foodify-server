package com.foodify.server.modules.restaurants.dto;

import com.foodify.server.modules.restaurants.domain.RestaurantCategory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record RestaurantDetailsResponse(
        Long id,
        String name,
        String description,
        // Multi-language support
        String nameEn,
        String nameFr,
        String nameAr,
        String descriptionEn,
        String descriptionFr,
        String descriptionAr,
        String imageUrl,
        String iconUrl,
        String address,
        String phone,
        Set<RestaurantCategory> restaurantCategories,
        Double rating,
        String openingHours,
        String closingHours,
        double latitude,
        double longitude,
        Double deliveryFee,
        Integer estimatedDeliveryTime,
        boolean favorite,
        List<RestaurantBadge> highlights,
        List<String> quickFilters,
        List<MenuItemSummary> topSales,
        List<MenuCategory> categories,
        List<WeeklyScheduleEntry> weeklySchedule,
        List<SpecialDay> specialDays
) {
    public record RestaurantBadge(String label, String value) {}

    public record MenuItemSummary(
            Long id,
            String name,
            String description,
            // Multi-language support
            String nameEn,
            String nameFr,
            String nameAr,
            String descriptionEn,
            String descriptionFr,
            String descriptionAr,
            double price,
            String imageUrl,
            boolean popular,
            List<String> tags,
            Double promotionPrice,
            String promotionLabel,
            boolean promotionActive,
            boolean favorite
    ) {}

    public record MenuCategory(String name, String nameEn, String nameFr, String nameAr, List<MenuItemDetails> items) {}

    public record MenuItemDetails(
            Long id,
            String name,
            String description,
            // Multi-language support
            String nameEn,
            String nameFr,
            String nameAr,
            String descriptionEn,
            String descriptionFr,
            String descriptionAr,
            double price,
            String imageUrl,
            boolean popular,
            List<String> tags,
            Double promotionPrice,
            String promotionLabel,
            boolean promotionActive,
            boolean favorite,
            List<MenuOptionGroupDto> optionGroups
    ) {}

    public record MenuOptionGroupDto(
            Long id,
            String name,
            // Multi-language support
            String nameEn,
            String nameFr,
            String nameAr,
            int minSelect,
            int maxSelect,
            boolean required,
            List<MenuItemExtraDto> extras
    ) {}

    public record MenuItemExtraDto(Long id, String name, String nameEn, String nameFr, String nameAr, double price, boolean defaultOption) {}

    public record WeeklyScheduleEntry(
            DayOfWeek day,
            boolean open,
            LocalTime opensAt,
            LocalTime closesAt
    ) {}

    public record SpecialDay(
            Long id,
            String name,
            LocalDate date,
            boolean open,
            LocalTime opensAt,
            LocalTime closesAt
    ) {}
}
