package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseCreationException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateWarehouseUseCaseUnitTest {

  @Test
  void createsWarehouseWhenLocationLimitsAllowIt() {
    var store = new RecordingWarehouseStore();
    var useCase =
        new CreateWarehouseUseCase(store, resolver(new Location("LOC-1", 2, 100)));
    var warehouse = warehouse("WH-1", "LOC-1", 40);

    useCase.create(warehouse);

    assertNotNull(warehouse.createdAt);
    assertSame(warehouse, store.created);
  }

  @Test
  void rejectsExistingBusinessUnitCode() {
    var store = new RecordingWarehouseStore();
    store.existing = warehouse("WH-1", "LOC-1", 20);
    var useCase =
        new CreateWarehouseUseCase(store, resolver(new Location("LOC-1", 2, 100)));

    var exception =
        assertThrows(
            WarehouseCreationException.class,
            () -> useCase.create(warehouse("WH-1", "LOC-1", 20)));

    assertEquals("Warehouse already exists", exception.getMessage());
  }

  @Test
  void rejectsCreationWhenWarehouseCountIsAtMaximum() {
    var store = new RecordingWarehouseStore();
    store.atLocation.add(warehouse("WH-1", "LOC-1", 20));
    var useCase =
        new CreateWarehouseUseCase(store, resolver(new Location("LOC-1", 1, 100)));

    var exception =
        assertThrows(
            WarehouseCreationException.class,
            () -> useCase.create(warehouse("WH-2", "LOC-1", 20)));

    assertEquals(
        "Maximum number of warehouses reached for this location: LOC-1",
        exception.getMessage());
  }

  @Test
  void rejectsCreationWhenAggregateCapacityWouldReachMaximum() {
    var store = new RecordingWarehouseStore();
    store.atLocation.add(warehouse("WH-1", "LOC-1", 60));
    var useCase =
        new CreateWarehouseUseCase(store, resolver(new Location("LOC-1", 3, 100)));

    var exception =
        assertThrows(
            WarehouseCreationException.class,
            () -> useCase.create(warehouse("WH-2", "LOC-1", 40)));

    assertEquals(
        "Adding this warehouse would exceed the maximum capacity for this location: LOC-1"
            + " Requested capacity: 100 Maximum capacity: 100",
        exception.getMessage());
  }

  private static LocationResolver resolver(Location location) {
    return identifier -> {
      assertEquals(location.identification, identifier);
      return location;
    };
  }

  private static Warehouse warehouse(String code, String location, int capacity) {
    var warehouse = new Warehouse();
    warehouse.businessUnitCode = code;
    warehouse.location = location;
    warehouse.capacity = capacity;
    warehouse.stock = 0;
    return warehouse;
  }

  private static class RecordingWarehouseStore implements WarehouseStore {

    private final List<Warehouse> atLocation = new ArrayList<>();
    private Warehouse existing;
    private Warehouse created;

    @Override
    public List<Warehouse> getAll() {
      return List.of();
    }

    @Override
    public void create(Warehouse warehouse) {
      created = warehouse;
    }

    @Override
    public void update(Warehouse warehouse) {}

    @Override
    public void remove(Warehouse warehouse) {}

    @Override
    public Warehouse findByBusinessUnitCode(String businessUnitCode) {
      return existing != null && existing.businessUnitCode.equals(businessUnitCode)
          ? existing
          : null;
    }

    @Override
    public List<Warehouse> findByLocationIdentifier(String location) {
      return atLocation.stream().filter(warehouse -> warehouse.location.equals(location)).toList();
    }
  }
}
