package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.util.List;

public interface FulfilmentUnitStore {
    List<FulfilmentUnit> getAll();
    List<FulfilmentUnit> findByProduct(Product product);
    List<FulfilmentUnit> findByProductAndStore(Product p, Store s);
    List<FulfilmentUnit> findByStore(Store s);
    List<FulfilmentUnit> findByWarehouse(Warehouse w);
    void create(FulfilmentUnit unit);
    void remove(FulfilmentUnit unit);
}

