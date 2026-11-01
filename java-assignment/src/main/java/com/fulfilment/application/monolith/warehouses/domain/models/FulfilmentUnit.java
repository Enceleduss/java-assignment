package com.fulfilment.application.monolith.warehouses.domain.models;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;

import java.time.LocalDateTime;

public class FulfilmentUnit {

  // unique identifier
  public Warehouse warehouse;

  public Product product;

  public Store store;

  public LocalDateTime assignedAt;

  public boolean isActive;
}
