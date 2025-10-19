package com.foodify.server.modules.restaurants.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record RestaurantDetailsResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        String iconUrl,
        String address,
        String phone,
        String type,
        Double rating,
        String openingHours,
        String closingHours,
        double latitude,
        double longitude,
        Double deliveryFee,
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
            double price,
            String imageUrl,
            boolean popular,
            List<String> tags,
            Double promotionPrice,
            String promotionLabel,
            boolean promotionActive,
            boolean favorite
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
            Double promotionPrice,
            String promotionLabel,
            boolean promotionActive,
            boolean favorite,
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
