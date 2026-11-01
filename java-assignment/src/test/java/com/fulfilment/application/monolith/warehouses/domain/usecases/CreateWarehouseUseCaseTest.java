package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseCreationException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CreateWarehouseUseCaseTest {

  @Inject CreateWarehouseUseCase createWarehouseUseCase;
  @Inject WarehouseRepository warehouseRepository;

  @Test
  @TestTransaction
  public void createsWarehouseAndSetsCreationTime() {
    var warehouse = warehouse("TEST-CREATE-001", "HELMOND-001", 40, 10);

    createWarehouseUseCase.create(warehouse);

    assertEquals(
        "TEST-CREATE-001",
        warehouseRepository.findByBusinessUnitCode("TEST-CREATE-001").businessUnitCode);
    assertNotNull(warehouse.createdAt);
  }

  @Test
  @TestTransaction
  public void rejectsDuplicateBusinessUnitCode() {
    warehouseRepository.create(warehouse("TEST-DUPLICATE-001", "HELMOND-001", 40, 10));

    var exception =
        assertThrows(
            WarehouseCreationException.class,
            () ->
                createWarehouseUseCase.create(
                    warehouse("TEST-DUPLICATE-001", "HELMOND-001", 25, 5)));

    assertEquals("Warehouse already exists", exception.getMessage());
  }

  @Test
  @TestTransaction
  public void rejectsCreationWhenLocationLimitsAreReached() {
    var warehouse = warehouse("TEST-LIMIT-001", "ZWOLLE-001", 25, 5);

    assertThrows(
        WarehouseCreationException.class,
        () -> createWarehouseUseCase.create(warehouse));
  }

  @Test
  @TestTransaction
  public void rejectsCreationWhenLocationCapacityIsReached() {
    warehouseRepository.create(warehouse("TEST-CAPACITY-001", "AMSTERDAM-002", 75, 10));

    assertThrows(
        WarehouseCreationException.class,
        () ->
            createWarehouseUseCase.create(
                warehouse("TEST-CAPACITY-002", "AMSTERDAM-002", 25, 5)));
  }

  private Warehouse warehouse(String code, String location, int capacity, int stock) {
    var warehouse = new Warehouse();
    warehouse.businessUnitCode = code;
    warehouse.location = location;
    warehouse.capacity = capacity;
    warehouse.stock = stock;
    return warehouse;
  }
}
