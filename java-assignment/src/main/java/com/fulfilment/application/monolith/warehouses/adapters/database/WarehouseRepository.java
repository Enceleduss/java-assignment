package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.api.mappers.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {
  @Inject
  WarehouseMapper warehouseMapper;
  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().filter(item -> item.archivedAt == null).map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    var dbWarehouse = DbWarehouse.fromWarehouse(warehouse);
    this.persist(dbWarehouse);
  }

  @Override
  public void update(Warehouse warehouse) {
    var existing = find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    if (existing == null) {
      throw new IllegalArgumentException("Warehouse with businessUnitCode " + warehouse.businessUnitCode + " not found");
    }
    //var dbExisting = this.findByBusinessUnitCode(warehouse.businessUnitCode);
    existing.location = warehouse.location;
    existing.capacity = warehouse.capacity;
    existing.stock = warehouse.stock;
    existing.archivedAt = warehouse.archivedAt;
    //this.persist(DbWarehouse.fromWarehouse(dbExisting));
  }

  @Override
  public void remove(Warehouse warehouse) {
    var dbWarehouse = find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    if (dbWarehouse != null) {
      this.delete(dbWarehouse);
    }
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    var dbWarehouse = find("businessUnitCode", buCode).stream().filter(item -> item.archivedAt == null).findAny();
      return dbWarehouse.map(DbWarehouse::toWarehouse).orElse(null);
  }

  @Override
  public List<Warehouse> findByLocationIdentifier(String loc) {
      return find("location", loc).stream().filter(item -> item.archivedAt == null).map(DbWarehouse::toWarehouse).toList();
  }
}
