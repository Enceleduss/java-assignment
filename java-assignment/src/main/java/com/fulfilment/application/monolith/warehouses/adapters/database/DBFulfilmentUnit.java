package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "fulfilment_unit",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_fulfilment_unit_warehouse_product_store",
            columnNames = {"warehouse_id", "product_id", "store_id"}))
@Cacheable
public class DBFulfilmentUnit {

  @Id @GeneratedValue public Long id;

  @ManyToOne
  @JoinColumn(name = "warehouse_id")
  public DbWarehouse warehouse;

  @ManyToOne
  @JoinColumn(name = "product_id")
  public Product product;

  @ManyToOne
  @JoinColumn(name = "store_id")
  public Store store;

  public LocalDateTime assignedAt;

  public boolean isActive;

  public DBFulfilmentUnit() {}

  public FulfilmentUnit toFulfilmentUnit() {
    var fulfilmentUnit = new FulfilmentUnit();
    fulfilmentUnit.warehouse = this.warehouse.toWarehouse();
    fulfilmentUnit.product = this.product;
    fulfilmentUnit.store = this.store;
    fulfilmentUnit.assignedAt = this.assignedAt;
    fulfilmentUnit.isActive = this.isActive;
    return fulfilmentUnit;
  }
}
