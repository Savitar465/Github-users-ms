package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.EquipoResponse;
import com.inspire.msusuarios.model.usuarios.Equipo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EquipoMapper {
    EquipoMapper INSTANCE = Mappers.getMapper(EquipoMapper.class);

    EquipoResponse equipoToEquipoResponse(Equipo equipo);

    default String map(Equipo value) {
        return value != null ? value.getEquipoId() : null;
    }
}
