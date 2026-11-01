package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ArchiveWarehouseUseCaseTest {

  @Inject ArchiveWarehouseUseCase archiveWarehouseUseCase;
  @Inject WarehouseRepository warehouseRepository;

  @Test
  @TestTransaction
  public void archivesWarehouseAndRemovesItFromActiveResults() {
    var warehouse = new Warehouse();
    warehouse.businessUnitCode = "TEST-ARCHIVE-001";
    warehouse.location = "HELMOND-001";
    warehouse.capacity = 40;
    warehouse.stock = 10;
    warehouseRepository.create(warehouse);

    archiveWarehouseUseCase.archive(warehouse);

    assertNotNull(warehouse.archivedAt);
    assertTrue(
        warehouseRepository.getAll().stream()
            .noneMatch(item -> item.businessUnitCode.equals("TEST-ARCHIVE-001")));
    assertNull(warehouseRepository.findByBusinessUnitCode("TEST-ARCHIVE-001"));
  }
}
