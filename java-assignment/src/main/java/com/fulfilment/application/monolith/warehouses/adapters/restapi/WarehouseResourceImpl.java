package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.api.mappers.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.domain.exceptions.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;
  @Inject private CreateWarehouseUseCase createWarehouseUseCase;
  @Inject private ArchiveWarehouseUseCase archiveWarehouseUseCase;
  @Inject private ReplaceWarehouseUseCase replaceWarehouseUseCase;
  @Inject WarehouseMapper warehouseMapper;

  @Override
  @Transactional
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  @Transactional
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    var domainWarehouse = warehouseMapper.toDomain(data);
    createWarehouseUseCase.create(domainWarehouse);
    return warehouseMapper.toApi(domainWarehouse);
  }



  @Override
  @Transactional
  public Warehouse getAWarehouseUnitByID(long id) {
    var dbWarehouse = warehouseRepository.findById(id);
    if (dbWarehouse == null) {
      throw new WarehouseNotFoundException("Warehouse with id " + id + " not found");
    }
    return warehouseMapper.toApi(dbWarehouse.toWarehouse());
  }

  @Override
  @Transactional
  public void archiveAWarehouseUnitByID(long id) {
    var dbWarehouse = warehouseRepository.findById(id);
    if (dbWarehouse == null) {
      throw new WarehouseNotFoundException("Warehouse with id " + id + " not found");
    }
    archiveWarehouseUseCase.archive(dbWarehouse.toWarehouse());
  }

  @Override
  @Transactional
  public Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode, @NotNull Warehouse data) {
    var newDomainWarehouse = warehouseMapper.toDomain(data);
    replaceWarehouseUseCase.replace(businessUnitCode, newDomainWarehouse);
    return warehouseMapper.toApi(newDomainWarehouse);
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }
}
