package com.foodify.server.modules.restaurants.sync;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogCdcListener {

    private final ObjectMapper objectMapper;
    private final CatalogSnapshotCache snapshotCache;

    @KafkaListener(topics = "#{catalogCdcProperties.menuItemTopic}")
    public void onMenuItemChange(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            CatalogMenuItemSnapshot snapshot = new CatalogMenuItemSnapshot(
                    asLong(root, "id"),
                    asLong(root, "restaurantId"),
                    asDouble(root, "price"),
                    asDoubleObject(root, "promotionPrice"),
                    asBoolean(root, "promotionActive"),
                    asBoolean(root, "available")
            );
            snapshotCache.putMenuItem(snapshot);
            log.debug("Catalog CDC synced menu item {}", snapshot.id());
        } catch (Exception ex) {
            log.warn("Failed to process menu item CDC payload: {}", message, ex);
        }
    }

    @KafkaListener(topics = "#{catalogCdcProperties.menuItemExtraTopic}")
    public void onMenuItemExtraChange(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            CatalogMenuItemExtraSnapshot snapshot = new CatalogMenuItemExtraSnapshot(
                    asLong(root, "id"),
                    asLong(root, "menuItemId"),
                    asText(root, "name"),
                    asDoubleObject(root, "price"),
                    asBoolean(root, "available")
            );
            snapshotCache.putExtra(snapshot);
            log.debug("Catalog CDC synced extra {}", snapshot.id());
        } catch (Exception ex) {
            log.warn("Failed to process menu item extra CDC payload: {}", message, ex);
        }
    }

    @KafkaListener(topics = "#{catalogCdcProperties.restaurantTopic}")
    public void onRestaurantChange(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            CatalogRestaurantSnapshot snapshot = new CatalogRestaurantSnapshot(
                    asLong(root, "id"),
                    asLong(root, "adminId"),
                    asText(root, "name"),
                    asText(root, "address"),
                    asText(root, "phone"),
                    asText(root, "imageUrl"),
                    asDoubleObject(root, "latitude"),
                    asDoubleObject(root, "longitude")
            );
            snapshotCache.putRestaurant(snapshot);
            log.debug("Catalog CDC synced restaurant {}", snapshot.id());
        } catch (Exception ex) {
            log.warn("Failed to process restaurant CDC payload: {}", message, ex);
        }
    }

    private Long asLong(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asLong() : null;
    }

    private Double asDouble(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asDouble() : 0.0;
    }

    private Double asDoubleObject(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asDouble() : null;
    }

    private Boolean asBoolean(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asBoolean() : null;
    }

    private String asText(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : null;
    }
}
