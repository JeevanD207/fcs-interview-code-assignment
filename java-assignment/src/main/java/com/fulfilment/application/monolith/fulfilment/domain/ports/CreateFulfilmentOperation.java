package com.fulfilment.application.monolith.fulfilment.domain.ports;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;

public interface CreateFulfilmentOperation {

    void create(Fulfilment fulfilment);
}