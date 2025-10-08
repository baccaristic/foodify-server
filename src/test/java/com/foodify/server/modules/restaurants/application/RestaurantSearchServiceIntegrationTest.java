package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchSort;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RestaurantSearchService.class)
class RestaurantSearchServiceIntegrationTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantSearchService restaurantSearchService;

    private Restaurant promotedRestaurant;

    @BeforeEach
    void setUp() {
        promotedRestaurant = new Restaurant();
        promotedRestaurant.setName("Promoted Place");
        promotedRestaurant.setTopChoice(true);
        promotedRestaurant.setFreeDelivery(true);
        promotedRestaurant.setTopEat(true);
        promotedRestaurant.setDeliveryFee(2.0);
        promotedRestaurant.setDeliveryTimeRange("25-35 min");
        promotedRestaurant.setRating(4.8);
        promotedRestaurant = restaurantRepository.save(promotedRestaurant);

        MenuItem promotedItem = new MenuItem();
        promotedItem.setName("Signature Dish");
        promotedItem.setPrice(12.0);
        promotedItem.setPromotionActive(true);
        promotedItem.setPromotionPrice(9.0);
        promotedItem.setPromotionLabel("LIMITED");
        promotedItem.setRestaurant(promotedRestaurant);
        menuItemRepository.save(promotedItem);

        Restaurant regularRestaurant = new Restaurant();
        regularRestaurant.setName("Regular Eats");
        regularRestaurant.setTopChoice(false);
        regularRestaurant.setFreeDelivery(false);
        regularRestaurant.setTopEat(false);
        regularRestaurant.setDeliveryFee(6.0);
        regularRestaurant.setDeliveryTimeRange("40-50 min");
        regularRestaurant.setRating(4.2);
        restaurantRepository.save(regularRestaurant);
    }

    @Test
    void searchFiltersByPromotionTopChoiceAndAvailability() {
        RestaurantSearchQuery query = new RestaurantSearchQuery(
                null,
                true,
                true,
                true,
                RestaurantSearchSort.POPULAR,
                true,
                5.0,
                1,
                10
        );

        PageResponse<RestaurantSearchItemDto> response = restaurantSearchService.search(query);

        assertThat(response.items()).hasSize(1);
        RestaurantSearchItemDto item = response.items().get(0);
        assertThat(item.id()).isEqualTo(promotedRestaurant.getId());
        assertThat(item.promotedMenuItems()).hasSize(1);
        assertThat(item.promotedMenuItems().get(0).promotionPrice()).isEqualTo(9.0);
    }

    @Test
    void searchRespectsDeliveryFeeThreshold() {
        RestaurantSearchQuery query = new RestaurantSearchQuery(
                null,
                null,
                null,
                null,
                RestaurantSearchSort.PICKED,
                null,
                1.0,
                1,
                10
        );

        PageResponse<RestaurantSearchItemDto> response = restaurantSearchService.search(query);

        assertThat(response.items()).isEmpty();
    }
}
