package com.fulfilment.application.monolith.warehouses.validator;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WarehouseValidatorTest {

    private final WarehouseValidator warehouseValidator =
            new WarehouseValidator();

    @Test
    void shouldThrowExceptionWhenBusinessUnitCodeIsNull() {

        Warehouse warehouse = warehouse(
                null,
                "ZWOLLE-001",
                20,
                10
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Business Unit Code is required",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenBusinessUnitCodeIsBlank() {

        Warehouse warehouse = warehouse(
                "   ",
                "ZWOLLE-001",
                20,
                10
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Business Unit Code is required",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenLocationIsNull() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                null,
                20,
                10
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Location is required",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenLocationIsBlank() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                "   ",
                20,
                10
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Location is required",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenCapacityIsNull() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                "ZWOLLE-001",
                null,
                10
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Capacity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenCapacityIsZero() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                "ZWOLLE-001",
                0,
                0
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Capacity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenCapacityIsNegative() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                "ZWOLLE-001",
                -10,
                0
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Capacity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenStockIsNull() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                "ZWOLLE-001",
                20,
                null
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Stock cannot be negative",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                "ZWOLLE-001",
                20,
                -1
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Stock cannot be negative",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenStockExceedsCapacity() {

        Warehouse warehouse = warehouse(
                "MWH-100",
                "ZWOLLE-001",
                20,
                25
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> warehouseValidator.validate(warehouse)
        );

        assertEquals(
                "Stock cannot exceed capacity",
                exception.getMessage()
        );
    }

    private Warehouse warehouse(
            String businessUnitCode,
            String location,
            Integer capacity,
            Integer stock) {

        Warehouse warehouse = new Warehouse();

        warehouse.businessUnitCode = businessUnitCode;
        warehouse.location = location;
        warehouse.capacity = capacity;
        warehouse.stock = stock;

        return warehouse;
    }
}