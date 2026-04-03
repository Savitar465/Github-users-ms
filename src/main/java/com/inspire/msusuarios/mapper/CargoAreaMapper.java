package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.CargoAreaResponse;
import com.inspire.msusuarios.model.usuarios.CargoArea;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CargoAreaMapper {
    CargoAreaResponse cargoAreaToCargoAreaResponse(CargoArea cargoArea);
}