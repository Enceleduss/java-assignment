package com.fulfilment.application.monolith.warehouses.adapters.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DbWarehouseTest {

  @Test
  void mapsAllWarehouseFieldsToAndFromDatabaseEntity() {
    var createdAt = LocalDateTime.of(2025, 1, 2, 3, 4);
    var archivedAt = LocalDateTime.of(2025, 2, 3, 4, 5);
    var warehouse = new Warehouse();
    warehouse.id = 42L;
    warehouse.businessUnitCode = "MWH.TEST";
    warehouse.location = "TEST-LOCATION";
    warehouse.capacity = 80;
    warehouse.stock = 25;
    warehouse.createdAt = createdAt;
    warehouse.archivedAt = archivedAt;

    var entity = DbWarehouse.fromWarehouse(warehouse);
    var result = entity.toWarehouse();

    assertEquals(warehouse.businessUnitCode, entity.businessUnitCode);
    assertEquals(warehouse.location, entity.location);
    assertEquals(warehouse.capacity, entity.capacity);
    assertEquals(warehouse.stock, entity.stock);
    assertEquals(warehouse.createdAt, entity.createdAt);
    assertEquals(warehouse.archivedAt, entity.archivedAt);
    assertEquals(entity.id, result.id);
    assertEquals(entity.businessUnitCode, result.businessUnitCode);
    assertEquals(entity.location, result.location);
    assertEquals(entity.capacity, result.capacity);
    assertEquals(entity.stock, result.stock);
    assertEquals(entity.createdAt, result.createdAt);
    assertEquals(entity.archivedAt, result.archivedAt);
  }
}
