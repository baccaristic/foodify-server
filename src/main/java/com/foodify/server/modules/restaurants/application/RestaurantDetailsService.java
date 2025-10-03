package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.MenuOptionGroup;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.RestaurantDetailsResponse;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantDetailsService {

    private final RestaurantRepository restaurantRepository;

    @Transactional()
    public RestaurantDetailsResponse getRestaurantDetails(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        List<MenuItem> menuItems = restaurant.getMenu() != null ? restaurant.getMenu() : List.of();

        List<RestaurantDetailsResponse.RestaurantBadge> highlights = buildHighlights(restaurant);
        Map<String, List<MenuItem>> itemsByCategory = groupByCategory(menuItems);
        List<String> quickFilters = buildQuickFilters(itemsByCategory);
        List<RestaurantDetailsResponse.MenuItemSummary> topSales = mapTopSales(menuItems);
        List<RestaurantDetailsResponse.MenuCategory> categories = mapCategories(itemsByCategory);

        return new RestaurantDetailsResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getImageUrl(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getType(),
                restaurant.getRating(),
                restaurant.getOpeningHours(),
                restaurant.getClosingHours(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                highlights,
                quickFilters,
                topSales,
                categories
        );
    }

    private List<RestaurantDetailsResponse.RestaurantBadge> buildHighlights(Restaurant restaurant) {
        List<RestaurantDetailsResponse.RestaurantBadge> highlights = new ArrayList<>();
        if (restaurant.getRating() != null) {
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Rating", restaurant.getRating()));
        }
        if (restaurant.getType() != null) {
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Category", restaurant.getType()));
        }
        if (restaurant.getOpeningHours() != null && restaurant.getClosingHours() != null) {
            String schedule = String.format(Locale.ROOT, "%s - %s", restaurant.getOpeningHours(), restaurant.getClosingHours());
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Hours", schedule));
        }
        if (restaurant.getAddress() != null) {
            highlights.add(new RestaurantDetailsResponse.RestaurantBadge("Address", restaurant.getAddress()));
        }
        return highlights;
    }

    private Map<String, List<MenuItem>> groupByCategory(List<MenuItem> menuItems) {
        return menuItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getCategory() != null ? item.getCategory() : "Other",
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
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

    private List<RestaurantDetailsResponse.MenuItemSummary> mapTopSales(List<MenuItem> menuItems) {
        return menuItems.stream()
                .filter(MenuItem::isPopular)
                .sorted(Comparator.comparing(MenuItem::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .map(this::toSummary)
                .toList();
    }

    private RestaurantDetailsResponse.MenuItemSummary toSummary(MenuItem item) {
        return new RestaurantDetailsResponse.MenuItemSummary(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                firstImage(item),
                item.isPopular(),
                buildTags(item)
        );
    }

    private List<RestaurantDetailsResponse.MenuCategory> mapCategories(Map<String, List<MenuItem>> itemsByCategory) {
        return itemsByCategory.entrySet().stream()
                .map(entry -> new RestaurantDetailsResponse.MenuCategory(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(this::toDetails)
                                .toList()
                ))
                .toList();
    }

    private RestaurantDetailsResponse.MenuItemDetails toDetails(MenuItem item) {
        return new RestaurantDetailsResponse.MenuItemDetails(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                firstImage(item),
                item.isPopular(),
                buildTags(item),
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

    private List<String> buildTags(MenuItem item) {
        Set<String> tags = new java.util.LinkedHashSet<>();
        if (item.getCategory() != null) {
            tags.add(item.getCategory());
        }
        if (item.isPopular()) {
            tags.add("Top Sales");
        }
        tags.add(item.getRestaurant().getName());
        return new ArrayList<>(tags);
    }

    private String firstImage(MenuItem item) {
        return (item.getImageUrls() != null && !item.getImageUrls().isEmpty())
                ? item.getImageUrls().get(0)
                : null;
    }
}
