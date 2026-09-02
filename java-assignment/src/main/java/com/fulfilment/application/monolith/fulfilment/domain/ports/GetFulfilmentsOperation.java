package com.fulfilment.application.monolith.fulfilment.domain.ports;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;

import java.util.List;

public interface GetFulfilmentsOperation {

    List<Fulfilment> getAll();
}