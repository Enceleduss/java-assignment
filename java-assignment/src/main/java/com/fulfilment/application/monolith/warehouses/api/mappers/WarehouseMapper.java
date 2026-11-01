package com.fulfilment.application.monolith.warehouses.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface WarehouseMapper {

    // Maps the OpenAPI bean to your Domain model
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomain(
            com.warehouse.api.beans.Warehouse apiWarehouse
    );

    // Maps the Domain model back to the OpenAPI bean if returning data
    com.warehouse.api.beans.Warehouse toApi(
            com.fulfilment.application.monolith.warehouses.domain.models.Warehouse domainWarehouse
    );
}
