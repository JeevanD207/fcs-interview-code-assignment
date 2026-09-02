package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.validator.WarehouseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReplaceWarehouseUseCaseTest {

    private WarehouseStore warehouseStore;
    private LocationResolver locationResolver;
    private ReplaceWarehouseUseCase replaceWarehouseUseCase;
    private WarehouseValidator warehouseValidator;

    @BeforeEach
    void setUp() {
        warehouseStore = mock(WarehouseStore.class);
        locationResolver = mock(LocationResolver.class);
        warehouseValidator =  mock(WarehouseValidator.class);

        replaceWarehouseUseCase =
                new ReplaceWarehouseUseCase(warehouseStore, locationResolver,warehouseValidator);
    }

    @Test
    void shouldReplaceWarehouseSuccessfully() {

        Warehouse oldWarehouse = warehouse(
                Long.valueOf("1"),
                "MWH-001",
                "AMSTERDAM-001",
                50,
                10
        );

        Warehouse newWarehouse = warehouse(
                Long.valueOf("2"),
                "MWH-001",
                "AMSTERDAM-002",
                30,
                10
        );

        Location location =
                new Location("AMSTERDAM-002", 5, 100);

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(oldWarehouse);

        when(locationResolver.resolveByIdentifier("AMSTERDAM-002"))
                .thenReturn(location);

        when(warehouseStore.getAll())
                .thenReturn(List.of(oldWarehouse));

        replaceWarehouseUseCase.replace(newWarehouse);

        verify(warehouseValidator).validate(newWarehouse);

        assertNotNull(oldWarehouse.archivedAt);
        assertNotNull(newWarehouse.createdAt);
        assertNull(newWarehouse.archivedAt);

        verify(warehouseStore).update(oldWarehouse);
        verify(warehouseStore).create(newWarehouse);
    }

    private Warehouse warehouse(
            Long id,
            String businessUnitCode,
            String location,
            Integer capacity,
            Integer stock) {

        Warehouse warehouse = new Warehouse();

        warehouse.id = id;
        warehouse.businessUnitCode = businessUnitCode;
        warehouse.location = location;
        warehouse.capacity = capacity;
        warehouse.stock = stock;

        return warehouse;
    }
}