package com.fulfilment.application.monolith.warehouses.validator;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseValidator {

    public void validate(Warehouse warehouse) {

        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse is required");
        }

        if (warehouse.businessUnitCode == null || warehouse.businessUnitCode.isBlank()) {
            throw new IllegalArgumentException("Business Unit Code is required");
        }

        if (warehouse.location == null || warehouse.location.isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }

        if (warehouse.capacity == null || warehouse.capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }

        if (warehouse.stock == null || warehouse.stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        if (warehouse.stock > warehouse.capacity) {
            throw new IllegalArgumentException("Stock cannot exceed capacity");
        }
    }
}