package com.fulfilment.application.monolith.warehouses.adapters.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DBFulfilmentUnitTest {

  @Test
  void mapsDatabaseEntityToDomainFulfilmentUnit() {
    var assignedAt = LocalDateTime.of(2025, 3, 4, 5, 6);
    var product = new Product("TEST-PRODUCT");
    var store = new Store("TEST-STORE");
    var warehouse = new DbWarehouse();
    warehouse.businessUnitCode = "MWH.TEST";
    warehouse.location = "TEST-LOCATION";
    warehouse.capacity = 80;
    warehouse.stock = 25;
    var entity = new DBFulfilmentUnit();
    entity.product = product;
    entity.store = store;
    entity.warehouse = warehouse;
    entity.assignedAt = assignedAt;
    entity.isActive = true;

    var result = entity.toFulfilmentUnit();

    assertSame(product, result.product);
    assertSame(store, result.store);
    assertEquals(warehouse.businessUnitCode, result.warehouse.businessUnitCode);
    assertEquals(warehouse.location, result.warehouse.location);
    assertEquals(warehouse.capacity, result.warehouse.capacity);
    assertEquals(warehouse.stock, result.warehouse.stock);
    assertEquals(assignedAt, result.assignedAt);
    assertTrue(result.isActive);
  }
}
