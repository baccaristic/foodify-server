package com.foodify.catalogservice.api;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ExtrasLookupRequest(@NotEmpty List<Long> ids) {
}
