package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.CreateFulfilmentOperation;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import com.fulfilment.application.monolith.fulfilment.validator.FulfilmentValidator;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.stores.StoreRepository;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateFulfilmentUseCase implements CreateFulfilmentOperation {

    private final FulfilmentStore fulfilmentStore;
    private final FulfilmentValidator fulfilmentValidator;
    private final ProductRepository productRepository;
    private final WarehouseStore warehouseStore;
    private final StoreRepository storeRepository;

    public CreateFulfilmentUseCase(
            FulfilmentStore fulfilmentStore,
            FulfilmentValidator fulfilmentValidator,
            ProductRepository productRepository,
            StoreRepository storeRepository,
            WarehouseStore warehouseStore) {

        this.fulfilmentStore = fulfilmentStore;
        this.fulfilmentValidator = fulfilmentValidator;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.warehouseStore = warehouseStore;
    }

    @Override
    public void create(Fulfilment fulfilment) {

        // First validate required IDs
        fulfilmentValidator.validate(
                fulfilment,
                fulfilmentStore.getAll()
        );

        validateProductExists(fulfilment.productId);
        validateStoreExists(fulfilment.storeId);
        validateWarehouseExists(fulfilment.warehouseId);

        fulfilmentStore.create(fulfilment);
    }

    private void validateProductExists(Long productId) {

        if (productRepository.findById(productId) == null) {
            throw new IllegalArgumentException(
                    "Product not found: " + productId
            );
        }
    }

    private void validateStoreExists(Long storeId) {

        if (storeRepository.findById(storeId) == null) {
            throw new IllegalArgumentException(
                    "Store not found: " + storeId
            );
        }
    }

    private void validateWarehouseExists(Long warehouseId) {

        if (warehouseStore.findWarehouseById(warehouseId) == null) {
            throw new IllegalArgumentException(
                    "Warehouse not found: " + warehouseId
            );
        }
    }
}