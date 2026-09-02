package com.fulfilment.application.monolith.fulfilment.validator;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FulfilmentValidator {

    public void validate(Fulfilment newFulfilment,
                         List<Fulfilment> existingFulfilments) {

        if (newFulfilment == null) {
            throw new IllegalArgumentException("Fulfilment is required");
        }

        if (newFulfilment.productId == null) {
            throw new IllegalArgumentException("Product is required");
        }

        if (newFulfilment.storeId == null) {
            throw new IllegalArgumentException("Store is required");
        }

        if (newFulfilment.warehouseId == null) {
            throw new IllegalArgumentException("Warehouse is required");
        }

        validateDuplicate(newFulfilment, existingFulfilments);
        validateWarehousesPerProductAndStore(
                newFulfilment,
                existingFulfilments
        );
        validateWarehousesPerStore(
                newFulfilment,
                existingFulfilments
        );
        validateProductsPerWarehouse(
                newFulfilment,
                existingFulfilments
        );
    }

    private void validateDuplicate(
            Fulfilment newFulfilment,
            List<Fulfilment> existingFulfilments) {

        boolean duplicateExists = existingFulfilments.stream()
                .anyMatch(existing ->
                        existing.productId.equals(newFulfilment.productId)
                                && existing.storeId.equals(newFulfilment.storeId)
                                && existing.warehouseId.equals(newFulfilment.warehouseId)
                );

        if (duplicateExists) {
            throw new IllegalArgumentException(
                    "Fulfilment association already exists"
            );
        }
    }

    private void validateWarehousesPerProductAndStore(
            Fulfilment newFulfilment,
            List<Fulfilment> existingFulfilments) {

        long warehouseCount = existingFulfilments.stream()
                .filter(existing ->
                        existing.productId.equals(newFulfilment.productId)
                                && existing.storeId.equals(newFulfilment.storeId)
                )
                .map(existing -> existing.warehouseId)
                .distinct()
                .count();

        if (warehouseCount >= 2) {
            throw new IllegalArgumentException(
                    "A product can be fulfilled by a maximum of 2 warehouses per store"
            );
        }
    }

    private void validateWarehousesPerStore(
            Fulfilment newFulfilment,
            List<Fulfilment> existingFulfilments) {

        boolean warehouseAlreadyAssociated = existingFulfilments.stream()
                .anyMatch(existing ->
                        existing.storeId.equals(newFulfilment.storeId)
                                && existing.warehouseId.equals(newFulfilment.warehouseId)
                );

        if (warehouseAlreadyAssociated) {
            return;
        }

        long warehouseCount = existingFulfilments.stream()
                .filter(existing ->
                        existing.storeId.equals(newFulfilment.storeId)
                )
                .map(existing -> existing.warehouseId)
                .distinct()
                .count();

        if (warehouseCount >= 3) {
            throw new IllegalArgumentException(
                    "A store can be fulfilled by a maximum of 3 warehouses"
            );
        }
    }

    private void validateProductsPerWarehouse(
            Fulfilment newFulfilment,
            List<Fulfilment> existingFulfilments) {

        boolean productAlreadyStored = existingFulfilments.stream()
                .anyMatch(existing ->
                        existing.warehouseId.equals(newFulfilment.warehouseId)
                                && existing.productId.equals(newFulfilment.productId)
                );

        if (productAlreadyStored) {
            return;
        }

        long productCount = existingFulfilments.stream()
                .filter(existing ->
                        existing.warehouseId.equals(newFulfilment.warehouseId)
                )
                .map(existing -> existing.productId)
                .distinct()
                .count();

        if (productCount >= 5) {
            throw new IllegalArgumentException(
                    "A warehouse can store a maximum of 5 product types"
            );
        }
    }
}