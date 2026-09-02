package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FulfilmentRepositoryTest {

    @Test
    void shouldPersistFulfilment() {

        FulfilmentRepository repository =
                spy(new FulfilmentRepository());

        Fulfilment fulfilment = new Fulfilment();

        doNothing()
                .when(repository)
                .persist(fulfilment);

        repository.create(fulfilment);

        verify(repository)
                .persist(fulfilment);
    }

    @Test
    void shouldReturnAllFulfilments() {

        FulfilmentRepository repository =
                spy(new FulfilmentRepository());

        Fulfilment fulfilment1 = new Fulfilment();
        Fulfilment fulfilment2 = new Fulfilment();

        List<Fulfilment> fulfilments =
                List.of(fulfilment1, fulfilment2);

        doReturn(fulfilments)
                .when(repository)
                .listAll();

        List<Fulfilment> result =
                repository.getAll();

        assertEquals(2, result.size());

        assertEquals(fulfilments, result);

        verify(repository)
                .listAll();
    }
}