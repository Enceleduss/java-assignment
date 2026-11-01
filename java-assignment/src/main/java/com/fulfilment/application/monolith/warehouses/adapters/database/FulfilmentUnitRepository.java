package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.FulfilmentUnitStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FulfilmentUnitRepository implements FulfilmentUnitStore, PanacheRepository<DBFulfilmentUnit> {
  @Inject ProductRepository productRepository;
  @Inject WarehouseRepository warehouseRepository;

  @Override
  public List<FulfilmentUnit> getAll() {
    return this.listAll().stream().map(DBFulfilmentUnit::toFulfilmentUnit).toList();
  }

  @Override
  public List<FulfilmentUnit> findByProduct(Product product) {
    return find("product.id", product.id).stream()
        .map(DBFulfilmentUnit::toFulfilmentUnit)
        .toList();
  }

  @Override
  public List<FulfilmentUnit> findByProductAndStore(Product p, Store s) {
    return find("product.id = ?1 and store.name = ?2", p.id, s.name).stream().map(DBFulfilmentUnit::toFulfilmentUnit).toList();
  }

  @Override
  public List<FulfilmentUnit> findByStore(Store s) {
    return find("store.name = ?1", s.name).stream().map(DBFulfilmentUnit::toFulfilmentUnit).toList();
  }

  @Override
  public List<FulfilmentUnit> findByWarehouse(Warehouse w) {
    return find("warehouse.businessUnitCode = ?1", w.businessUnitCode).stream().map(DBFulfilmentUnit::toFulfilmentUnit).toList();
  }

  @Override
  public void create(FulfilmentUnit fulfilmentUnit) {
    var dbFulfilmentUnit = new DBFulfilmentUnit();
    dbFulfilmentUnit.product = productRepository.findById(fulfilmentUnit.product.id);
    dbFulfilmentUnit.store = Store.find("name", fulfilmentUnit.store.name).firstResult();
    dbFulfilmentUnit.warehouse =
        warehouseRepository
            .find(
                "businessUnitCode = ?1 and archivedAt is null",
                fulfilmentUnit.warehouse.businessUnitCode)
            .firstResult();

    if (dbFulfilmentUnit.product == null
        || dbFulfilmentUnit.store == null
        || dbFulfilmentUnit.warehouse == null) {
      throw new IllegalArgumentException(
          "Fulfilment unit product, store, and active warehouse must exist");
    }

    dbFulfilmentUnit.assignedAt = fulfilmentUnit.assignedAt;
    dbFulfilmentUnit.isActive = fulfilmentUnit.isActive;
    this.persist(dbFulfilmentUnit);
  }

  @Override
  public void remove(FulfilmentUnit unit) {

  }
}
