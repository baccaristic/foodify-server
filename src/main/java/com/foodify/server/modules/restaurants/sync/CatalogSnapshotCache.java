package com.foodify.server.modules.restaurants.sync;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CatalogSnapshotCache {

    private final ConcurrentMap<Long, CatalogMenuItemSnapshot> menuItems = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, CatalogMenuItemExtraSnapshot> extras = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, CatalogRestaurantSnapshot> restaurants = new ConcurrentHashMap<>();

    public void putMenuItem(CatalogMenuItemSnapshot snapshot) {
        if (snapshot == null || snapshot.id() == null) {
            return;
        }
        menuItems.put(snapshot.id(), snapshot);
    }

    public Optional<CatalogMenuItemSnapshot> getMenuItem(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(menuItems.get(id));
    }

    public void putExtra(CatalogMenuItemExtraSnapshot snapshot) {
        if (snapshot == null || snapshot.id() == null) {
            return;
        }
        extras.put(snapshot.id(), snapshot);
    }

    public Optional<CatalogMenuItemExtraSnapshot> getExtra(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(extras.get(id));
    }

    public void putRestaurant(CatalogRestaurantSnapshot snapshot) {
        if (snapshot == null || snapshot.id() == null) {
            return;
        }
        restaurants.put(snapshot.id(), snapshot);
    }

    public Optional<CatalogRestaurantSnapshot> getRestaurant(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(restaurants.get(id));
    }
}
