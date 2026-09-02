package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.validator.WarehouseValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

    private final WarehouseStore warehouseStore;
    private final LocationResolver locationResolver;
    private final WarehouseValidator warehouseValidator;

    public ReplaceWarehouseUseCase(
            WarehouseStore warehouseStore,
            LocationResolver locationResolver,
            WarehouseValidator warehouseValidator) {
        this.warehouseStore = warehouseStore;
        this.locationResolver = locationResolver;
        this.warehouseValidator = warehouseValidator;
    }

    @Override
    @Transactional
    public void replace(Warehouse newWarehouse) {

        warehouseValidator.validate(newWarehouse);

        Warehouse oldWarehouse =
                warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);

        if (oldWarehouse == null) {
            throw new IllegalArgumentException(
                    "Active warehouse not found: " + newWarehouse.businessUnitCode);
        }

        Location location = locationResolver.resolveByIdentifier(newWarehouse.location);
        if (location == null) {
            throw new IllegalArgumentException("Invalid location: " + newWarehouse.location);
        }

        if (!newWarehouse.stock.equals(oldWarehouse.stock)) {
            throw new IllegalArgumentException(
                    "Replacement warehouse stock must match the previous warehouse stock");
        }

        if (newWarehouse.capacity < oldWarehouse.stock) {
            throw new IllegalArgumentException(
                    "Replacement warehouse capacity cannot accommodate previous stock");
        }

        var otherWarehousesAtLocation =
                warehouseStore.getAll().stream()
                        .filter(existing -> !existing.id.equals(oldWarehouse.id))
                        .filter(existing -> existing.location.equals(newWarehouse.location))
                        .toList();

        if (otherWarehousesAtLocation.size() >= location.maxNumberOfWarehouses) {
            throw new IllegalArgumentException(
                    "Maximum number of warehouses reached for location: " + newWarehouse.location);
        }

        int totalCapacity =
                otherWarehousesAtLocation.stream()
                        .mapToInt(existing -> existing.capacity)
                        .sum()
                        + newWarehouse.capacity;

        if (totalCapacity > location.maxCapacity) {
            throw new IllegalArgumentException(
                    "Maximum capacity exceeded for location: " + newWarehouse.location);
        }

        oldWarehouse.archivedAt = LocalDateTime.now();
        warehouseStore.update(oldWarehouse);

        newWarehouse.createdAt = LocalDateTime.now();
        newWarehouse.archivedAt = null;
        warehouseStore.create(newWarehouse);
    }
}