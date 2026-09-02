package com.fulfilment.application.monolith.fulfilment.adapters.restapi;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.CreateFulfilmentOperation;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FulfilmentResourceTest {

    private FulfilmentResource fulfilmentResource;
    private CreateFulfilmentOperation createFulfilmentOperation;

    @BeforeEach
    void setUp() {

        fulfilmentResource = new FulfilmentResource();

        createFulfilmentOperation =
                mock(CreateFulfilmentOperation.class);

        fulfilmentResource.createFulfilmentOperation =
                createFulfilmentOperation;
    }

    @Test
    void shouldCreateFulfilmentSuccessfully() {

        Fulfilment fulfilment = mock(Fulfilment.class);

        Response response =
                fulfilmentResource.create(fulfilment);

        assertEquals(
                Response.Status.CREATED.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                fulfilment,
                response.getEntity()
        );

        verify(createFulfilmentOperation)
                .create(fulfilment);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreateFails() {

        Fulfilment fulfilment = mock(Fulfilment.class);

        doThrow(new IllegalArgumentException(
                "Fulfilment association already exists"
        ))
                .when(createFulfilmentOperation)
                .create(fulfilment);

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> fulfilmentResource.create(fulfilment)
                );

        assertEquals(
                "Fulfilment association already exists",
                exception.getMessage()
        );

        verify(createFulfilmentOperation)
                .create(fulfilment);
    }
}