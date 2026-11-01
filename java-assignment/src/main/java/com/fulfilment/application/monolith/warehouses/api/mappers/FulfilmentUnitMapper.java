package com.fulfilment.application.monolith.warehouses.api.mappers;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface FulfilmentUnitMapper {

    // Maps the OpenAPI bean to your Domain model
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "store.name", source = "storeId")
    @Mapping(target = "warehouse.businessUnitCode", source = "warehouseBusinessUnitCode")
    com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit toDomain(
            com.warehouse.api.beans.FulfilmentUnit apiFulfilmentUnit
    );

    // Maps the Domain model back to the OpenAPI bean if returning data
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "storeId", source = "store.name")
    @Mapping(target = "warehouseBusinessUnitCode", source = "warehouse.businessUnitCode")
    com.warehouse.api.beans.FulfilmentUnit toApi(
            com.fulfilment.application.monolith.warehouses.domain.models.FulfilmentUnit domainFulfilmentUnit
    );
}
