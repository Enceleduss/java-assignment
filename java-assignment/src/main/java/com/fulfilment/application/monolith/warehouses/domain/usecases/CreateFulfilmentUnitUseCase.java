package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.ports.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@ApplicationScoped
public class CreateFulfilmentUnitUseCase implements CreateFulfilmentUnitOperation {

  private final FulfilmentUnitStore fulfilmentUnitStore;
  @Inject
  private final FulfilmentConstraintValidator constraintValidator;
  public CreateFulfilmentUnitUseCase(FulfilmentUnitStore fulfilmentUnitStore, FulfilmentConstraintValidator constraintValidator) {
    this.fulfilmentUnitStore = fulfilmentUnitStore;
    this.constraintValidator = constraintValidator;
  }

  @Override
  public void create(FulfilmentUnit fulfilmentUnit) {
    constraintValidator.validateBeforeCreate(fulfilmentUnit);
    fulfilmentUnit.assignedAt = LocalDateTime.now();
    fulfilmentUnit.isActive = true;
    fulfilmentUnitStore.create(fulfilmentUnit);
  }
}
