package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseCreationException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      throw new WarehouseCreationException("Warehouse already exists");
    }
    if (warehouseStore.findByLocationIdentifier(warehouse.location).size() >= locationResolver.resolveByIdentifier(warehouse.location).maxNumberOfWarehouses) {
      throw new WarehouseCreationException("Maximum number of warehouses reached for this location: " + warehouse.location);
    }
    int totalCapacitySoFar = warehouseStore.findByLocationIdentifier(warehouse.location).stream().mapToInt(item -> item.capacity).sum();
    totalCapacitySoFar += warehouse.capacity;
    int maxCapacity = locationResolver.resolveByIdentifier(warehouse.location).maxCapacity;
    if (totalCapacitySoFar >= maxCapacity) {
      throw new WarehouseCreationException("Adding this warehouse would exceed the maximum capacity for this location: " + warehouse.location+" Requested capacity: "+totalCapacitySoFar+" Maximum capacity: "+maxCapacity);
    }
    warehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(warehouse);
  }
}
