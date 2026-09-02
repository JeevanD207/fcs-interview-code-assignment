package com.fulfilment.application.monolith.stores;

public interface StoreRepository {

    Store findById(Long storeId);
}