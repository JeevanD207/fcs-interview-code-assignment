package com.fulfilment.application.monolith.fulfilment.domain.ports;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;

import java.util.List;

public interface FulfilmentStore {

    void create(Fulfilment fulfilment);

    List<Fulfilment> getAll();
}