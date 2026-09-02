package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import com.fulfilment.application.monolith.fulfilment.validator.FulfilmentValidator;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.stores.StoreRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CreateFulfilmentUseCaseTest {

    private final FulfilmentStore fulfilmentStore =
            mock(FulfilmentStore.class);

    private final FulfilmentValidator fulfilmentValidator =
            mock(FulfilmentValidator.class);

    private final ProductRepository productRepository =
            mock(ProductRepository.class);

    private final StoreRepository storeRepository =
            mock(StoreRepository.class);

    private final WarehouseStore warehouseStore =
            mock(WarehouseStore.class);

    private final CreateFulfilmentUseCase useCase =
            new CreateFulfilmentUseCase(
                    fulfilmentStore,
                    fulfilmentValidator,
                    productRepository,
                    storeRepository,
                    warehouseStore
            );

    @Test
    void shouldCreateFulfilmentSuccessfully() {

        Fulfilment fulfilment = fulfilment(1L, 1L, 1L);

        Product product = new Product("Test Product");
        Store store = mock(Store.class);
        Warehouse warehouse = mock(Warehouse.class);

        when(productRepository.findById(1L))
                .thenReturn(product);

        when(storeRepository.findById(1L))
                .thenReturn(store);

        when(warehouseStore.findWarehouseById(1L))
                .thenReturn(warehouse);

        useCase.create(fulfilment);

        verify(fulfilmentValidator)
                .validate(eq(fulfilment), any());

        verify(fulfilmentStore)
                .create(fulfilment);
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        Fulfilment fulfilment = fulfilment(1L, 1L, 1L);

        when(productRepository.findById(1L))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(fulfilment)
        );

        assertEquals(
                "Product not found: 1",
                exception.getMessage()
        );

        verify(fulfilmentStore, never())
                .create(any());
    }

    @Test
    void shouldThrowExceptionWhenStoreDoesNotExist() {

        Fulfilment fulfilment = fulfilment(1L, 1L, 1L);

        Product product = new Product("Test Product");

        when(productRepository.findById(1L))
                .thenReturn(product);

        when(storeRepository.findById(1L))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(fulfilment)
        );

        assertEquals(
                "Store not found: 1",
                exception.getMessage()
        );

        verify(fulfilmentStore, never())
                .create(any());
    }

    @Test
    void shouldThrowExceptionWhenWarehouseDoesNotExist() {

        Fulfilment fulfilment = fulfilment(1L, 1L, 1L);

        Product product = new Product("Test Product");
        Store store = mock(Store.class);

        when(productRepository.findById(1L))
                .thenReturn(product);

        when(storeRepository.findById(1L))
                .thenReturn(store);

        when(warehouseStore.findWarehouseById(1L))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(fulfilment)
        );

        assertEquals(
                "Warehouse not found: 1",
                exception.getMessage()
        );

        verify(fulfilmentStore, never())
                .create(any());
    }

    @Test
    void shouldNotCreateFulfilmentWhenValidatorThrowsException() {

        Fulfilment fulfilment = fulfilment(1L, 1L, 1L);

        doThrow(new IllegalArgumentException("Validation failed"))
                .when(fulfilmentValidator)
                .validate(any(), any());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(fulfilment)
        );

        assertEquals(
                "Validation failed",
                exception.getMessage()
        );

        verify(productRepository, never())
                .findById(anyLong());

        verify(storeRepository, never())
                .findById(anyLong());

        verify(warehouseStore, never())
                .findWarehouseById(anyLong());

        verify(fulfilmentStore, never())
                .create(any());
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