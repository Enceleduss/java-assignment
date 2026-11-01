package com.fulfilment.application.monolith.warehouses.adapters.errors;

import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseCreationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WarehouseCreationExceptionMapper implements ExceptionMapper<WarehouseCreationException> {
    @Override
    public Response toResponse(WarehouseCreationException exception) {
        ErrorResponse error = new ErrorResponse(exception.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
