package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class FulfilmentRepository
        implements PanacheRepository<Fulfilment>, FulfilmentStore {

    @Override
    @Transactional
    public void create(Fulfilment fulfilment) {
        persist(fulfilment);
    }

    @Override
    public List<Fulfilment> getAll() {
        return listAll();
    }
}