package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.validator.WarehouseValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

    private final WarehouseStore warehouseStore;
    private final LocationResolver locationResolver;
    private final WarehouseValidator warehouseValidator;

    public CreateWarehouseUseCase(
            WarehouseStore warehouseStore,
            LocationResolver locationResolver,
            WarehouseValidator warehouseValidator) {
        this.warehouseStore = warehouseStore;
        this.locationResolver = locationResolver;
        this.warehouseValidator = warehouseValidator;
    }

    @Override
    @Transactional
    public void create(Warehouse warehouse) {

        warehouseValidator.validate(warehouse);

        if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
            throw new IllegalArgumentException(
                    "Business Unit Code already exists: " + warehouse.businessUnitCode);
        }

        Location location = locationResolver.resolveByIdentifier(warehouse.location);
        if (location == null) {
            throw new IllegalArgumentException("Invalid location: " + warehouse.location);
        }

        var warehousesAtLocation =
                warehouseStore.getAll().stream()
                        .filter(existing -> existing.location.equals(warehouse.location))
                        .toList();

        if (warehousesAtLocation.size() >= location.maxNumberOfWarehouses) {
            throw new IllegalArgumentException(
                    "Maximum number of warehouses reached for location: " + warehouse.location);
        }

        int totalCapacity =
                warehousesAtLocation.stream()
                        .mapToInt(existing -> existing.capacity)
                        .sum()
                        + warehouse.capacity;

        if (totalCapacity > location.maxCapacity) {
            throw new IllegalArgumentException(
                    "Maximum capacity exceeded for location: " + warehouse.location);
        }

        warehouse.createdAt = LocalDateTime.now();
        warehouse.archivedAt = null;

        warehouseStore.create(warehouse);
    }
}