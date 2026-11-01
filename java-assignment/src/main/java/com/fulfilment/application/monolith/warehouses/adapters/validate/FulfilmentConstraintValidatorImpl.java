package com.fulfilment.application.monolith.warehouses.adapters.validate;

import com.fulfilment.application.monolith.warehouses.domain.exceptions.FulfilmentUnitCreationException;
import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.ports.FulfilmentConstraintValidator;
import com.fulfilment.application.monolith.warehouses.domain.ports.FulfilmentUnitStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FulfilmentConstraintValidatorImpl implements FulfilmentConstraintValidator {
    private final FulfilmentUnitStore fulfilmentUnitStore;

    public FulfilmentConstraintValidatorImpl(FulfilmentUnitStore fulfilmentUnitStore) {
        this.fulfilmentUnitStore = fulfilmentUnitStore;
    }

    @Override
    public void validateBeforeCreate(FulfilmentUnit unit) {

        long warehousesForProductAtStore =
                fulfilmentUnitStore.findByProductAndStore(unit.product, unit.store)
                        .stream().filter(item -> item.isActive)
                        .count();
        if (warehousesForProductAtStore >= 2) {
            throw new FulfilmentUnitCreationException(
                    "Product " + unit.product.name + " at Store " + unit.store.name +
                            " already has 2 warehouses assigned");
        }

        // Constraint 2: Max 3 warehouses per store
        long distinctWarehousesForStore =
                fulfilmentUnitStore.findByStore(unit.store)
                        .stream().filter(item -> item.isActive)
                        .map(u -> u.warehouse.businessUnitCode)
                        .distinct().count();
        if (distinctWarehousesForStore >= 3) {
            throw new FulfilmentUnitCreationException(
                    "Store " + unit.store.name + " already has 3 warehouses assigned");
        }

        // Constraint 3: Max 5 products per warehouse
        long distinctProductsForWarehouse =
                fulfilmentUnitStore.findByWarehouse(unit.warehouse)
                        .stream().filter(item -> item.isActive)
                        .map(u -> u.product.id)
                        .distinct().count();
        if (distinctProductsForWarehouse >= 5) {
            throw new FulfilmentUnitCreationException(
                    "Warehouse " + unit.warehouse.businessUnitCode +
                            " already stores 5 product types");
        }
    }
}
