package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseReplacementException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ReplaceWarehouseUseCaseTest {

  @Inject ReplaceWarehouseUseCase replaceWarehouseUseCase;
  @Inject WarehouseRepository warehouseRepository;

  @Test
  @TestTransaction
  public void archivesExistingWarehouseAndCreatesReplacement() {
    var existing = warehouse("TEST-REPLACE-001", 40, 10);
    warehouseRepository.create(existing);
    var replacement = warehouse("IGNORED-CODE", 50, 10);

    replaceWarehouseUseCase.replace("TEST-REPLACE-001", replacement);

    var versions = warehouseRepository.find("businessUnitCode", "TEST-REPLACE-001").list();
    assertEquals(2, versions.size());
    assertTrue(versions.stream().anyMatch(version -> version.archivedAt != null));
    assertTrue(versions.stream().anyMatch(version -> version.archivedAt == null));
    assertNotNull(replacement.createdAt);
    assertEquals("TEST-REPLACE-001", replacement.businessUnitCode);
    assertEquals(
        "TEST-REPLACE-001",
        warehouseRepository.findByBusinessUnitCode("TEST-REPLACE-001").businessUnitCode);
  }

  @Test
  @TestTransaction
  public void rejectsReplacementWhenWarehouseDoesNotExist() {
    assertThrows(
        WarehouseNotFoundException.class,
        () ->
            replaceWarehouseUseCase.replace(
                "TEST-MISSING-001", warehouse("TEST-MISSING-001", 100, 10)));
  }

  @Test
  @TestTransaction
  public void rejectsReplacementWithCapacityBelowExistingStock() {
    warehouseRepository.create(warehouse("TEST-REPLACE-CAPACITY-001", 40, 10));

    assertThrows(
        WarehouseReplacementException.class,
        () ->
            replaceWarehouseUseCase.replace(
                "TEST-REPLACE-CAPACITY-001", warehouse("TEST-REPLACE-CAPACITY-001", 5, 10)));
  }

  @Test
  @TestTransaction
  public void rejectsReplacementWithDifferentStock() {
    warehouseRepository.create(warehouse("TEST-REPLACE-STOCK-001", 40, 10));

    assertThrows(
        WarehouseReplacementException.class,
        () ->
            replaceWarehouseUseCase.replace(
                "TEST-REPLACE-STOCK-001",
                warehouse("TEST-REPLACE-STOCK-001", 50, 9)));
  }

  private Warehouse warehouse(String code, int capacity, int stock) {
    var warehouse = new Warehouse();
    warehouse.businessUnitCode = code;
    warehouse.location = "LOC-1";
    warehouse.capacity = capacity;
    warehouse.stock = stock;
    return warehouse;
  }
}
