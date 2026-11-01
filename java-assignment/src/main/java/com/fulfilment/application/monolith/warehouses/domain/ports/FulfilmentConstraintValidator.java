package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;

public interface FulfilmentConstraintValidator {
    void validateBeforeCreate(FulfilmentUnit unit);
}
