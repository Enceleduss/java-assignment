package com.fulfilment.application.monolith.warehouses.api.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import org.junit.jupiter.api.Test;

class FulfilmentUnitMapperTest {

  private final FulfilmentUnitMapper mapper = new FulfilmentUnitMapperImpl();

  @Test
  void mapsApiFulfilmentUnitToDomain() {
    var apiUnit = new com.warehouse.api.beans.FulfilmentUnit();
    apiUnit.setProductId(12L);
    apiUnit.setStoreId("STORE-1");
    apiUnit.setWarehouseBusinessUnitCode("WH-1");

    FulfilmentUnit result = mapper.toDomain(apiUnit);

    assertNotNull(result.product);
    assertEquals(12L, result.product.id);
    assertNotNull(result.store);
    assertEquals("STORE-1", result.store.name);
    assertNotNull(result.warehouse);
    assertEquals("WH-1", result.warehouse.businessUnitCode);
  }

  @Test
  void mapsDomainFulfilmentUnitToApi() {
    var domainUnit = new FulfilmentUnit();
    domainUnit.product = new Product("PRODUCT-1");
    domainUnit.product.id = 12L;
    domainUnit.store = new Store("STORE-1");
    domainUnit.warehouse = new Warehouse();
    domainUnit.warehouse.businessUnitCode = "WH-1";

    var result = mapper.toApi(domainUnit);

    assertEquals(12L, result.getProductId());
    assertEquals("STORE-1", result.getStoreId());
    assertEquals("WH-1", result.getWarehouseBusinessUnitCode());
  }
}
