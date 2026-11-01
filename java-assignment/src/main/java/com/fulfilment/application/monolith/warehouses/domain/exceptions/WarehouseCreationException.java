package com.fulfilment.application.monolith.warehouses.domain.exceptions;

public class WarehouseCreationException extends RuntimeException {
    public WarehouseCreationException(String message) {
        super(message);
    }
}
