package com.fulfilment.application.monolith.warehouses.adapters.errors;

import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WarehouseNotFoundExceptionMapper implements ExceptionMapper<WarehouseNotFoundException> {
    @Override
    public Response toResponse(WarehouseNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
