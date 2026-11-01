package com.fulfilment.application.monolith.warehouses.adapters.validate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.FulfilmentUnitRepository;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.exceptions.FulfilmentUnitCreationException;
import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@QuarkusTest
class FulfilmentConstraintValidatorImplTest {

  @Inject FulfilmentConstraintValidatorImpl validator;
  @Inject FulfilmentUnitRepository fulfilmentUnitRepository;
  @Inject ProductRepository productRepository;
  @Inject WarehouseRepository warehouseRepository;

  @Test
  @TestTransaction
  void allowsFulfilmentWhenAllLimitsAreBelowTheirMaximum() {
    var product = createProduct();
    var store = createStore();
    var warehouse = warehouse("MWH.012");

    assertDoesNotThrow(() -> validator.validateBeforeCreate(unit(product, store, warehouse)));
  }

  @Test
  @TestTransaction
  void rejectsThirdWarehouseForProductAtStore() {
    var product = createProduct();
    var store = createStore();
    createAssociation(product, store, warehouse("MWH.001"));
    createAssociation(product, store, warehouse("MWH.012"));

    var exception =
        assertThrows(
            FulfilmentUnitCreationException.class,
            () -> validator.validateBeforeCreate(unit(product, store, warehouse("MWH.023"))));

    assertEquals(
        "Product " + product.name + " at Store " + store.name + " already has 2 warehouses assigned",
        exception.getMessage());
  }

  @Test
  @TestTransaction
  void rejectsFourthWarehouseForStore() {
    var store = createStore();
    createAssociation(createProduct(), store, warehouse("MWH.001"));
    createAssociation(createProduct(), store, warehouse("MWH.012"));
    createAssociation(createProduct(), store, warehouse("MWH.023"));
    var candidateProduct = createProduct();

    var exception =
        assertThrows(
            FulfilmentUnitCreationException.class,
            () ->
                validator.validateBeforeCreate(
                    unit(candidateProduct, store, warehouse("MWH.001"))));

    assertEquals("Store " + store.name + " already has 3 warehouses assigned", exception.getMessage());
  }

  @Test
  @TestTransaction
  void rejectsSixthProductForWarehouse() {
    var warehouse = warehouse("MWH.012");
    for (int i = 0; i < 5; i++) {
      createAssociation(createProduct(), createStore(), warehouse);
    }
    var candidateProduct = createProduct();
    var candidateStore = createStore();

    var exception =
        assertThrows(
            FulfilmentUnitCreationException.class,
            () ->
                validator.validateBeforeCreate(
                    unit(candidateProduct, candidateStore, warehouse)));

    assertEquals(
        "Warehouse " + warehouse.businessUnitCode + " already stores 5 product types",
        exception.getMessage());
  }

  private Product createProduct() {
    var product = new Product("test-product-" + UUID.randomUUID().toString().substring(0, 8));
    productRepository.persist(product);
    return product;
  }

  private Store createStore() {
    var store = new Store("test-store-" + UUID.randomUUID().toString().substring(0, 8));
    store.persist();
    return store;
  }

  private Warehouse warehouse(String businessUnitCode) {
    return warehouseRepository.findByBusinessUnitCode(businessUnitCode);
  }

  private void createAssociation(Product product, Store store, Warehouse warehouse) {
    var fulfilmentUnit = unit(product, store, warehouse);
    fulfilmentUnitRepository.create(fulfilmentUnit);
  }

  private FulfilmentUnit unit(Product product, Store store, Warehouse warehouse) {
    var fulfilmentUnit = new FulfilmentUnit();
    fulfilmentUnit.product = product;
    fulfilmentUnit.store = store;
    fulfilmentUnit.warehouse = warehouse;
    fulfilmentUnit.assignedAt = LocalDateTime.now();
    fulfilmentUnit.isActive = true;
    return fulfilmentUnit;
  }
}
