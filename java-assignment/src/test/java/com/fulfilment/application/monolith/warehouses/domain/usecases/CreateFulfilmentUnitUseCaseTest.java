package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.FulfilmentUnitStore;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateFulfilmentUnitUseCaseTest {

  @Test
  void validatesAndCreatesAnActiveFulfilmentUnit() {
    var store = new RecordingFulfilmentUnitStore();
    var unit = new FulfilmentUnit();
    var validationCalled = new boolean[1];
    var useCase =
        new CreateFulfilmentUnitUseCase(
            store, ignored -> validationCalled[0] = true);

    useCase.create(unit);

    assertTrue(validationCalled[0]);
    assertNotNull(unit.assignedAt);
    assertTrue(unit.isActive);
    assertSame(unit, store.createdUnit);
  }

  @Test
  void doesNotPersistUnitWhenValidationFails() {
    var store = new RecordingFulfilmentUnitStore();
    var unit = new FulfilmentUnit();
    var useCase =
        new CreateFulfilmentUnitUseCase(
            store,
            ignored -> {
              throw new IllegalArgumentException("Invalid fulfilment unit");
            });

    assertThrows(IllegalArgumentException.class, () -> useCase.create(unit));

    assertFalse(unit.isActive);
    assertNull(unit.assignedAt);
    assertNull(store.createdUnit);
  }

  private static class RecordingFulfilmentUnitStore implements FulfilmentUnitStore {

    private FulfilmentUnit createdUnit;

    @Override
    public List<FulfilmentUnit> getAll() {
      return List.of();
    }

    @Override
    public List<FulfilmentUnit> findByProduct(Product product) {
      return List.of();
    }

    @Override
    public List<FulfilmentUnit> findByProductAndStore(Product product, Store store) {
      return List.of();
    }

    @Override
    public List<FulfilmentUnit> findByStore(Store store) {
      return List.of();
    }

    @Override
    public List<FulfilmentUnit> findByWarehouse(Warehouse warehouse) {
      return List.of();
    }

    @Override
    public void create(FulfilmentUnit unit) {
      createdUnit = unit;
    }

    @Override
    public void remove(FulfilmentUnit unit) {
      throw new UnsupportedOperationException();
    }
  }
}
