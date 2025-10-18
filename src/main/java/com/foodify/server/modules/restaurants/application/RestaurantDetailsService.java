package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuCategory;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.MenuOptionGroup;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.domain.RestaurantSpecialDay;
import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import com.foodify.server.modules.restaurants.dto.RestaurantDetailsResponse;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantOperatingHourRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantSpecialDayRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantDetailsService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantOperatingHourRepository restaurantOperatingHourRepository;
    private final RestaurantSpecialDayRepository restaurantSpecialDayRepository;
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    @Transactional()
    public RestaurantDetailsResponse getRestaurantDetails(Long restaurantId) {
        return getRestaurantDetails(restaurantId, null, null, Set.of(), Set.of());
    }

    @Transactional()
    public RestaurantDetailsResponse getRestaurantDetails(Long restaurantId, Double clientLatitude, Double clientLongitude) {
        return getRestaurantDetails(restaurantId, clientLatitude, clientLongitude, Set.of(), Set.of());
    }

    @Transactional()
    public RestaurantDetailsResponse getRestaurantDetails(Long restaurantId, Set<Long> favoriteRestaurantIds, Set<Long> favoriteMenuItemIds) {
        return getRestaurantDetails(restaurantId, null, null, favoriteRestaurantIds, favoriteMenuItemIds);
    }

    @Transactional()
    public RestaurantDetailsResponse getRestaurantDetails(Long restaurantId, Double clientLatitude, Double clientLongitude, Set<Long> favoriteRestaurantIds, Set<Long> favoriteMenuItemIds) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        Set<Long> restaurantFavorites = favoriteRestaurantIds == null ? Set.of() : favoriteRestaurantIds;
        Set<Long> menuItemFavorites = favoriteMenuItemIds == null ? Set.of() : favoriteMenuItemIds;

        List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdAndAvailableTrue(restaurantId);
        List<RestaurantWeeklyOperatingHour> weeklyHours = restaurantOperatingHourRepository
                .findByRestaurant_IdOrderByDayOfWeekAsc(restaurantId);
        List<RestaurantSpecialDay> specialDays = restaurantSpecialDayRepository
                .findByRestaurant_IdOrderByDateAsc(restaurantId);

        Map<DayOfWeek, RestaurantWeeklyOperatingHour> weeklyByDay = weeklyHours.stream()
                .collect(Collectors.toMap(RestaurantWeeklyOperatingHour::getDayOfWeek, Function.identity(), (left, right) -> left));

        List<RestaurantDetailsResponse.WeeklyScheduleEntry> weeklySchedule = Arrays.stream(DayOfWeek.values())
                .map(day -> {
                    RestaurantWeeklyOperatingHour hour = weeklyByDay.get(day);
                    if (hour == null) {
                        return new RestaurantDetailsResponse.WeeklyScheduleEntry(day, false, null, null);
                    }
                    return toWeeklyScheduleEntry(hour);
                })
                .toList();

        List<RestaurantDetailsResponse.SpecialDay> specialDayDtos = specialDays.stream()
                .map(this::toSpecialDayDto)
                .toList();

        Optional<RestaurantWeeklyOperatingHour> firstOpen = firstOpenDay(weeklyHours);
        String openingHours = firstOpen.map(RestaurantWeeklyOperatingHour::getOpensAt).map(this::formatTime)
                .orElse(restaurant.getOpeningHours());
        String closingHours = firstOpen.map(RestaurantWeeklyOperatingHour::getClosesAt).map(this::formatTime)
                .orElse(restaurant.getClosingHours());

        List<RestaurantDetailsResponse.RestaurantBadge> highlights = buildHighlights(restaurant, weeklyHours);
        Map<String, List<MenuItem>> itemsByCategory = groupByCategory(menuItems);
        List<String> quickFilters = buildQuickFilters(itemsByCategory);
        List<RestaurantDetailsResponse.MenuItemSummary> topSales = mapTopSales(menuItems, menuItemFavorites);
        List<RestaurantDetailsResponse.MenuCategory> categories = mapCategories(itemsByCategory, menuItemFavorites);
        Double deliveryFee = deliveryFeeCalculator.calculateFee(
                clientLatitude,
                clientLongitude,
                restaurant.getLatitude(),
                restaurant.getLongitude()
        ).orElse(null);

        return new RestaurantDetailsResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getImageUrl(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getType(),
                restaurant.getRating(),
                openingHours,
                closingHours,
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                deliveryFee,
                restaurantFavorites.contains(restaurant.getId()),
                highlights,
                quickFilters,
                topSales,
                categories,
                weeklySchedule,
                specialDayDtos
        );
    }

    private List<RestaurantDetailsResponse.RestaurantBadge> buildHighlights(Restaurant restaurant, List<RestaurantWeeklyOperatingHour> weeklyHours) {
        List<RestaurantDetailsResponse.RestaurantBadge> highlights = new ArrayList<>();
        if (restaurant.getRating() != null) {
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Rating", formatRating(restaurant.getRating())));
        }
        if (restaurant.getType() != null) {
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Category", restaurant.getType()));
        }
        Optional<RestaurantWeeklyOperatingHour> firstOpen = firstOpenDay(weeklyHours);
        if (firstOpen.isPresent()) {
            RestaurantWeeklyOperatingHour hour = firstOpen.get();
            String dayLabel = hour.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ROOT);
            String schedule = String.format(Locale.ROOT, "%s %s - %s", dayLabel, formatTime(hour.getOpensAt()), formatTime(hour.getClosesAt()));
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Hours", schedule));
        } else if (restaurant.getOpeningHours() != null && restaurant.getClosingHours() != null) {
            String schedule = String.format(Locale.ROOT, "%s - %s", restaurant.getOpeningHours(), restaurant.getClosingHours());
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Hours", schedule));
        }
        if (restaurant.getAddress() != null) {
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Address", restaurant.getAddress()));
        }
        return highlights;
    }

    private String formatRating(Double rating) {
        return rating == null ? null : String.format(Locale.ROOT, "%.1f", rating);
    }

    private Map<String, List<MenuItem>> groupByCategory(List<MenuItem> menuItems) {
        Map<String, List<MenuItem>> grouped = new LinkedHashMap<>();
        for (MenuItem item : menuItems) {
            Set<MenuCategory> categories = item.getCategories();
            if (categories == null || categories.isEmpty()) {
                grouped.computeIfAbsent("Other", key -> new ArrayList<>()).add(item);
                continue;
            }

            categories.stream()
                    .map(MenuCategory::getName)
                    .map(name -> name == null || name.isBlank() ? "Other" : name)
                    .collect(Collectors.toCollection(LinkedHashSet::new))
                    .forEach(name -> grouped.computeIfAbsent(name, key -> new ArrayList<>()).add(item));
        }
        return grouped;
    }

    private List<String> buildQuickFilters(Map<String, List<MenuItem>> itemsByCategory) {
        if (itemsByCategory.isEmpty()) {
            return List.of();
        }
        List<String> filters = new ArrayList<>();
        filters.add("Top Sales");
        filters.addAll(itemsByCategory.keySet());
        return filters;
    }

    private List<RestaurantDetailsResponse.MenuItemSummary> mapTopSales(List<MenuItem> menuItems, Set<Long> favoriteMenuItemIds) {
        return menuItems.stream()
                .filter(MenuItem::isPopular)
                .sorted(Comparator.comparing(MenuItem::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .map(item -> toSummary(item, favoriteMenuItemIds))
                .toList();
    }

    private RestaurantDetailsResponse.MenuItemSummary toSummary(MenuItem item, Set<Long> favoriteMenuItemIds) {
        boolean promotionActive = Boolean.TRUE.equals(item.getPromotionActive());
        return new RestaurantDetailsResponse.MenuItemSummary(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                firstImage(item),
                item.isPopular(),
                buildTags(item, promotionActive),
                item.getPromotionPrice(),
                item.getPromotionLabel(),
                promotionActive,
                favoriteMenuItemIds.contains(item.getId())
        );
    }

    private List<RestaurantDetailsResponse.MenuCategory> mapCategories(Map<String, List<MenuItem>> itemsByCategory, Set<Long> favoriteMenuItemIds) {
        return itemsByCategory.entrySet().stream()
                .map(entry -> new RestaurantDetailsResponse.MenuCategory(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(item -> toDetails(item, favoriteMenuItemIds))
                                .toList()
                ))
                .toList();
    }

    private RestaurantDetailsResponse.MenuItemDetails toDetails(MenuItem item, Set<Long> favoriteMenuItemIds) {
        boolean promotionActive = Boolean.TRUE.equals(item.getPromotionActive());
        return new RestaurantDetailsResponse.MenuItemDetails(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                firstImage(item),
                item.isPopular(),
                buildTags(item, promotionActive),
                item.getPromotionPrice(),
                item.getPromotionLabel(),
                promotionActive,
                favoriteMenuItemIds.contains(item.getId()),
                item.getOptionGroups() == null ? List.of() : item.getOptionGroups().stream()
                        .map(this::toOptionGroup)
                        .toList()
        );
    }

    private RestaurantDetailsResponse.MenuOptionGroupDto toOptionGroup(MenuOptionGroup group) {
        return new RestaurantDetailsResponse.MenuOptionGroupDto(
                group.getId(),
                group.getName(),
                group.getMinSelect(),
                group.getMaxSelect(),
                group.isRequired(),
                group.getExtras() == null ? List.of() : group.getExtras().stream()
                        .map(this::toExtra)
                        .toList()
        );
    }

    private RestaurantDetailsResponse.MenuItemExtraDto toExtra(MenuItemExtra extra) {
        return new RestaurantDetailsResponse.MenuItemExtraDto(
                extra.getId(),
                extra.getName(),
                extra.getPrice(),
                extra.isDefault()
        );
    }

    private RestaurantDetailsResponse.WeeklyScheduleEntry toWeeklyScheduleEntry(RestaurantWeeklyOperatingHour hour) {
        return new RestaurantDetailsResponse.WeeklyScheduleEntry(
                hour.getDayOfWeek(),
                hour.isOpen(),
                hour.isOpen() ? hour.getOpensAt() : null,
                hour.isOpen() ? hour.getClosesAt() : null
        );
    }

    private RestaurantDetailsResponse.SpecialDay toSpecialDayDto(RestaurantSpecialDay specialDay) {
        return new RestaurantDetailsResponse.SpecialDay(
                specialDay.getId(),
                specialDay.getName(),
                specialDay.getDate(),
                specialDay.isOpen(),
                specialDay.isOpen() ? specialDay.getOpensAt() : null,
                specialDay.isOpen() ? specialDay.getClosesAt() : null
        );
    }

    private Optional<RestaurantWeeklyOperatingHour> firstOpenDay(List<RestaurantWeeklyOperatingHour> weeklyHours) {
        return weeklyHours.stream()
                .filter(RestaurantWeeklyOperatingHour::isOpen)
                .sorted(Comparator.comparing(RestaurantWeeklyOperatingHour::getDayOfWeek))
                .findFirst();
    }

    private String formatTime(LocalTime time) {
        return time == null ? null : time.toString();
    }

    private List<String> buildTags(MenuItem item, boolean promotionActive) {
        Set<String> tags = new java.util.LinkedHashSet<>();
        if (item.getCategories() != null) {
            item.getCategories().stream()
                    .map(MenuCategory::getName)
                    .filter(Objects::nonNull)
                    .filter(name -> !name.isBlank())
                    .forEach(tags::add);
        }
        if (item.isPopular()) {
            tags.add("Top Sales");
        }
        if (promotionActive) {
            tags.add("Promo");
        }
        if (item.getRestaurant() != null && item.getRestaurant().getName() != null) {
            tags.add(item.getRestaurant().getName());
        }
        return new ArrayList<>(tags);
    }

    private String firstImage(MenuItem item) {
        return (item.getImageUrls() != null && !item.getImageUrls().isEmpty())
                ? item.getImageUrls().get(0)
                : null;
    }
}
