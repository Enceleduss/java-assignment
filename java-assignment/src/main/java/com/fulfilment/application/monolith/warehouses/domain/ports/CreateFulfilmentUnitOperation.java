package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;

public interface CreateFulfilmentUnitOperation {
  void create(FulfilmentUnit fulfilmentUnit);
}
