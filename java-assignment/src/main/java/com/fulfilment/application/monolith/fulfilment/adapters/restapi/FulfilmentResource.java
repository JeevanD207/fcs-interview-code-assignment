package com.fulfilment.application.monolith.fulfilment.adapters.restapi;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.CreateFulfilmentOperation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fulfilment.application.monolith.fulfilment.domain.ports.GetFulfilmentsOperation;
import jakarta.ws.rs.GET;

import java.util.List;

@Path("/fulfilments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class FulfilmentResource {

    @Inject
    CreateFulfilmentOperation createFulfilmentOperation;

    @Inject
    GetFulfilmentsOperation getFulfilmentsOperation;

    @GET
    public List<Fulfilment> getAll() {
        return getFulfilmentsOperation.getAll();
    }

    @POST
    public Response create(Fulfilment fulfilment) {

        try {
            createFulfilmentOperation.create(fulfilment);

            return Response.status(Response.Status.CREATED)
                    .entity(fulfilment)
                    .build();

        } catch (IllegalArgumentException exception) {

            throw new BadRequestException(
                    exception.getMessage(),
                    exception
            );
        }
    }
}