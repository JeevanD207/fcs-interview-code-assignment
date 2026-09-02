package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import com.fulfilment.application.monolith.fulfilment.domain.ports.GetFulfilmentsOperation;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GetFulfilmentsUseCase implements GetFulfilmentsOperation {

    private final FulfilmentStore fulfilmentStore;

    public GetFulfilmentsUseCase(FulfilmentStore fulfilmentStore) {
        this.fulfilmentStore = fulfilmentStore;
    }

    @Override
    public List<Fulfilment> getAll() {
        return fulfilmentStore.getAll();
    }
}