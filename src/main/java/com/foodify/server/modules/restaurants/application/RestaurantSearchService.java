package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchSort;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final RestaurantRepository restaurantRepository;

    public PageResponse<RestaurantSearchItemDto> search(RestaurantSearchQuery query) {
        Specification<Restaurant> specification = buildSpecification(query);
        Sort sort = toSort(query.sort());
        int page = query.page() != null && query.page() > 0 ? query.page() : 1;
        int pageSize = query.pageSize() != null && query.pageSize() > 0 ? query.pageSize() : 20;
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);

        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        List<RestaurantSearchItemDto> items = restaurants.getContent().stream()
                .map(this::toDto)
                .toList();

        return new PageResponse<>(items, page, pageSize, restaurants.getTotalElements());
    }

    private RestaurantSearchItemDto toDto(Restaurant restaurant) {
        return new RestaurantSearchItemDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDeliveryTimeRange(),
                restaurant.getRating(),
                Boolean.TRUE.equals(restaurant.getTopChoice()),
                Boolean.TRUE.equals(restaurant.getFreeDelivery()),
                restaurant.getPromotionLabel(),
                restaurant.getImageUrl()
        );
    }

    private Sort toSort(RestaurantSearchSort sort) {
        RestaurantSearchSort effectiveSort = sort == null ? RestaurantSearchSort.PICKED : sort;
        return switch (effectiveSort) {
            case POPULAR -> Sort.by(Sort.Order.desc("topEat"), Sort.Order.desc("rating"), Sort.Order.asc("name"));
            case RATING -> Sort.by(Sort.Order.desc("rating"), Sort.Order.asc("name"));
            case PICKED -> Sort.by(Sort.Order.desc("topChoice"), Sort.Order.desc("rating"), Sort.Order.asc("name"));
        };
    }

    private Specification<Restaurant> buildSpecification(RestaurantSearchQuery query) {
        Specification<Restaurant> specification = Specification.where(null);
        if (query == null) {
            return specification;
        }

        if (query.query() != null && !query.query().isBlank()) {
            String like = "%" + query.query().toLowerCase(Locale.ROOT) + "%";
            specification = specification.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like)
            ));
        }

        if (Boolean.TRUE.equals(query.hasPromotion())) {
            specification = specification.and((root, cq, cb) -> cb.and(
                    cb.isNotNull(root.get("promotionLabel")),
                    cb.notEqual(cb.lower(root.get("promotionLabel")), "")
            ));
        }

        if (Boolean.TRUE.equals(query.isTopChoice())) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("topChoice")));
        }

        if (Boolean.TRUE.equals(query.hasFreeDelivery())) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("freeDelivery")));
        }

        if (Boolean.TRUE.equals(query.topEatOnly())) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("topEat")));
        }

        if (query.maxDeliveryFee() != null) {
            specification = specification.and((root, cq, cb) -> cb.or(
                    cb.isNull(root.get("deliveryFee")),
                    cb.lessThanOrEqualTo(root.get("deliveryFee"), query.maxDeliveryFee())
            ));
        }

        return specification;
    }
}
