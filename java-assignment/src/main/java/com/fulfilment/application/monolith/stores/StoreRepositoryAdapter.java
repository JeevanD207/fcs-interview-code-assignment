package com.fulfilment.application.monolith.stores;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StoreRepositoryAdapter implements StoreRepository {

    @Override
    public Store findById(Long storeId) {
        return Store.findById(storeId);
    }
}