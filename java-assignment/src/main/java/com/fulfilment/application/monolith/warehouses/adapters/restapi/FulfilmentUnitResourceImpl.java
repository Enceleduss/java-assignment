package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.FulfilmentUnitRepository;
import com.fulfilment.application.monolith.warehouses.api.mappers.FulfilmentUnitMapper;

import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateFulfilmentUnitUseCase;
import com.warehouse.api.FulfilmentResource;
import com.warehouse.api.beans.FulfilmentUnit;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class FulfilmentUnitResourceImpl implements FulfilmentResource {

  @Inject private FulfilmentUnitRepository fulfilmentUnitRepository;
  @Inject private CreateFulfilmentUnitUseCase createFulfilmentUnitUseCase;
  //@Inject private ArchiveFulfilmentUnitUseCase archiveFulfilmentUnitUseCase;
  //@Inject private ReplaceFulfilmentUnitUseCase replaceFulfilmentUnitUseCase;
  @Inject
  FulfilmentUnitMapper fulfilmentUnitMapper;

  /*@Override
  public List<Warehouse> listAllWarehousesUnits() {
    return fulfilmentUnitRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    var domainWarehouse = warehouseMapper.toDomain(data);
    createWarehouseUseCase.create(domainWarehouse);
    return warehouseMapper.toApi(domainWarehouse);
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    var domainWarehouse = warehouseRepository.findByBusinessUnitCode(id);
    if (domainWarehouse == null) {
      throw new jakarta.ws.rs.NotFoundException("Warehouse with id " + id + " not found");
    }
    return warehouseMapper.toApi(domainWarehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    var domainWarehouse = warehouseRepository.findByBusinessUnitCode(id);
    if (domainWarehouse == null) {
      throw new jakarta.ws.rs.NotFoundException("Warehouse with id " + id + " not found");
    }
    archiveWarehouseUseCase.archive(domainWarehouse);
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode, @NotNull Warehouse data) {
    var newDomainWarehouse = warehouseMapper.toDomain(data);
    replaceWarehouseUseCase.replace(businessUnitCode, newDomainWarehouse);
    return warehouseMapper.toApi(newDomainWarehouse);
  }

  private FulfilmentUnit toFulfilmentResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit fulfilmentUnit) {
    var response = new FulfilmentUnit();
    response.setProductId(fulfilmentUnit.productId);
    response.setStoreId(fulfilmentUnit.storeId);
    response.setWarehouseBusinessUnitCode(fulfilmentUnit.warehouseBusinessUnitCode);
    return response;
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
*/
  private FulfilmentUnit toFulfilmentResponse(
          com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit fulfilmentUnit) {
    var response = new FulfilmentUnit();
    response.setProductId(fulfilmentUnit.product.id);
    response.setStoreId(fulfilmentUnit.store.name);
    response.setWarehouseBusinessUnitCode(fulfilmentUnit.warehouse.businessUnitCode);
    return response;
  }

  @Override
  public List<FulfilmentUnit> listAllFulfilmentAssociations() {
    return fulfilmentUnitRepository.getAll().stream().map(this::toFulfilmentResponse).toList();
  }

  @Override
  @Transactional
  public void createANewFulfilmentAssociation(FulfilmentUnit data) {
    createFulfilmentUnitUseCase.create(fulfilmentUnitMapper.toDomain(data));
  }


  @Override
  public List<FulfilmentUnit> getAllFulfilmentsForAWarehouse(String businessUnitCode) {
    //return fulfilmentUnitRepository.findByWarehouseBusinessUnitCode(businessUnitCode).stream().map(this::toFulfilmentResponse).toList();
      return List.of();
  }

  @Override
  public List<FulfilmentUnit> getAllFulfilmentsForAStore(String storeId) {
    return List.of();
  }

  @Override
  public List<Warehouse> getWarehousesFulfillingAProductAtAStore(long productId, String storeId) {
    return List.of();
  }

}
