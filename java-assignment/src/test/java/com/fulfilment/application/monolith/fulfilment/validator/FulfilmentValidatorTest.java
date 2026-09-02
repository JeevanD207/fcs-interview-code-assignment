package com.fulfilment.application.monolith.fulfilment.validator;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FulfilmentValidatorTest {

    private final FulfilmentValidator validator = new FulfilmentValidator();

    @Test
    void shouldAcceptValidFulfilment() {

        Fulfilment fulfilment = fulfilment(1L, 1L, 1L);

        assertDoesNotThrow(() ->
                validator.validate(fulfilment, List.of())
        );
    }

    @Test
    void shouldThrowExceptionWhenFulfilmentIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(null, List.of())
        );

        assertEquals("Fulfilment is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductIsNull() {

        Fulfilment fulfilment = fulfilment(null, 1L, 1L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(fulfilment, List.of())
        );

        assertEquals("Product is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStoreIsNull() {

        Fulfilment fulfilment = fulfilment(1L, null, 1L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(fulfilment, List.of())
        );

        assertEquals("Store is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWarehouseIsNull() {

        Fulfilment fulfilment = fulfilment(1L, 1L, null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(fulfilment, List.of())
        );

        assertEquals("Warehouse is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFulfilmentAlreadyExists() {

        Fulfilment existing = fulfilment(1L, 1L, 1L);
        Fulfilment newFulfilment = fulfilment(1L, 1L, 1L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        newFulfilment,
                        List.of(existing)
                )
        );

        assertEquals(
                "Fulfilment association already exists",
                exception.getMessage()
        );
    }

    @Test
    void shouldAllowMaximumTwoWarehousesForSameProductAndStore() {

        List<Fulfilment> existing = List.of(
                fulfilment(1L, 1L, 1L)
        );

        Fulfilment newFulfilment = fulfilment(1L, 1L, 2L);

        assertDoesNotThrow(() ->
                validator.validate(newFulfilment, existing)
        );
    }

    @Test
    void shouldThrowExceptionWhenMoreThanTwoWarehousesForSameProductAndStore() {

        List<Fulfilment> existing = List.of(
                fulfilment(1L, 1L, 1L),
                fulfilment(1L, 1L, 2L)
        );

        Fulfilment newFulfilment = fulfilment(1L, 1L, 3L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(newFulfilment, existing)
        );

        assertEquals(
                "A product can be fulfilled by a maximum of 2 warehouses per store",
                exception.getMessage()
        );
    }

    @Test
    void shouldAllowMaximumThreeWarehousesForSameStore() {

        List<Fulfilment> existing = List.of(
                fulfilment(1L, 1L, 1L),
                fulfilment(2L, 1L, 2L)
        );

        Fulfilment newFulfilment = fulfilment(3L, 1L, 3L);

        assertDoesNotThrow(() ->
                validator.validate(newFulfilment, existing)
        );
    }

    @Test
    void shouldThrowExceptionWhenMoreThanThreeWarehousesForSameStore() {

        List<Fulfilment> existing = List.of(
                fulfilment(1L, 1L, 1L),
                fulfilment(2L, 1L, 2L),
                fulfilment(3L, 1L, 3L)
        );

        Fulfilment newFulfilment = fulfilment(4L, 1L, 4L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(newFulfilment, existing)
        );

        assertEquals(
                "A store can be fulfilled by a maximum of 3 warehouses",
                exception.getMessage()
        );
    }

    @Test
    void shouldAllowMaximumFiveProductTypesForSameWarehouse() {

        List<Fulfilment> existing = List.of(
                fulfilment(1L, 1L, 1L),
                fulfilment(2L, 1L, 1L),
                fulfilment(3L, 1L, 1L),
                fulfilment(4L, 1L, 1L)
        );

        Fulfilment newFulfilment = fulfilment(5L, 1L, 1L);

        assertDoesNotThrow(() ->
                validator.validate(newFulfilment, existing)
        );
    }

    @Test
    void shouldThrowExceptionWhenMoreThanFiveProductTypesForSameWarehouse() {

        List<Fulfilment> existing = List.of(
                fulfilment(1L, 1L, 1L),
                fulfilment(2L, 1L, 1L),
                fulfilment(3L, 1L, 1L),
                fulfilment(4L, 1L, 1L),
                fulfilment(5L, 1L, 1L)
        );

        Fulfilment newFulfilment = fulfilment(6L, 1L, 1L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(newFulfilment, existing)
        );

        assertEquals(
                "A warehouse can store a maximum of 5 product types",
                exception.getMessage()
        );
    }

    private Fulfilment fulfilment(
            Long productId,
            Long storeId,
            Long warehouseId) {

        Fulfilment fulfilment = new Fulfilment();
        fulfilment.productId = productId;
        fulfilment.storeId = storeId;
        fulfilment.warehouseId = warehouseId;

        return fulfilment;
    }
}