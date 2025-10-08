package com.foodify.server.modules.restaurants.config;

import com.foodify.server.modules.restaurants.application.LocalRestaurantCatalogService;
import com.foodify.server.modules.restaurants.application.RemoteRestaurantCatalogService;
import com.foodify.server.modules.restaurants.application.RestaurantCatalogService;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(CatalogServiceProperties.class)
public class RestaurantCatalogConfiguration {

    @Bean
    @ConditionalOnProperty(name = "catalog.service.mode", havingValue = "remote")
    public RestClient catalogRestClient(ObjectProvider<RestClient.Builder> builderProvider,
                                        CatalogServiceProperties properties) {
        RestClient.Builder builder = builderProvider.getIfAvailable(RestClient::builder);
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) properties.getRemote().getConnectTimeout().toMillis());
        factory.setReadTimeout((int) properties.getRemote().getReadTimeout().toMillis());
        return builder
                .baseUrl(properties.getRemote().getBaseUrl())
                .requestFactory(factory)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "catalog.service.mode", havingValue = "remote")
    public RestaurantCatalogService remoteRestaurantCatalogService(RestClient catalogRestClient,
                                                                   RestaurantRepository restaurantRepository,
                                                                   MenuItemRepository menuItemRepository,
                                                                   MenuItemExtraRepository menuItemExtraRepository) {
        return new RemoteRestaurantCatalogService(catalogRestClient, restaurantRepository,
                menuItemRepository, menuItemExtraRepository);
    }

    @Bean
    @ConditionalOnMissingBean(RestaurantCatalogService.class)
    public RestaurantCatalogService localRestaurantCatalogService(RestaurantRepository restaurantRepository,
                                                                  MenuItemRepository menuItemRepository,
                                                                  MenuItemExtraRepository menuItemExtraRepository) {
        return new LocalRestaurantCatalogService(restaurantRepository, menuItemRepository, menuItemExtraRepository);
    }
}
