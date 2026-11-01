package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseReplacementException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Objects;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void replace(String businessUnitCode, Warehouse newWarehouse) {
    // Find the existing warehouse by business unit code
    Warehouse existingWarehouse = warehouseStore.findByBusinessUnitCode(businessUnitCode);
    if (existingWarehouse == null) {
      throw new WarehouseNotFoundException("Warehouse with businessUnitCode " + businessUnitCode + " not found");
    }
    if(newWarehouse.capacity < existingWarehouse.stock) {
      throw new WarehouseReplacementException("New warehouse capacity: " + newWarehouse.capacity + " is less than existing stock: " + existingWarehouse.stock);
    }
    if(!Objects.equals(newWarehouse.stock, existingWarehouse.stock)) {
      throw new WarehouseReplacementException("New warehouse stock: " + newWarehouse.stock + " does not match with existing warehouse stock: " + existingWarehouse.stock);
    }
    // Archive the existing warehouse
    existingWarehouse.archivedAt = LocalDateTime.now();
    warehouseStore.update(existingWarehouse);

    // Create the new warehouse with the same business unit code
    newWarehouse.businessUnitCode = businessUnitCode;
    newWarehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(newWarehouse);
  }
}
