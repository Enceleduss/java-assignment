package com.fulfilment.application.monolith.warehouses.api.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import org.junit.jupiter.api.Test;

class WarehouseMapperTest {

  private final WarehouseMapper mapper = new WarehouseMapperImpl();

  @Test
  void mapsApiWarehouseToDomain() {
    var apiWarehouse = new com.warehouse.api.beans.Warehouse();
    apiWarehouse.setBusinessUnitCode("WH-1");
    apiWarehouse.setLocation("LOC-1");
    apiWarehouse.setCapacity(80);
    apiWarehouse.setStock(25);

    Warehouse result = mapper.toDomain(apiWarehouse);

    assertEquals("WH-1", result.businessUnitCode);
    assertEquals("LOC-1", result.location);
    assertEquals(80, result.capacity);
    assertEquals(25, result.stock);
  }

  @Test
  void mapsDomainWarehouseToApi() {
    var domainWarehouse = new Warehouse();
    domainWarehouse.businessUnitCode = "WH-1";
    domainWarehouse.location = "LOC-1";
    domainWarehouse.capacity = 80;
    domainWarehouse.stock = 25;

    var result = mapper.toApi(domainWarehouse);

    assertEquals("WH-1", result.getBusinessUnitCode());
    assertEquals("LOC-1", result.getLocation());
    assertEquals(80, result.getCapacity());
    assertEquals(25, result.getStock());
  }
}
