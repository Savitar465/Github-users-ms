package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.RolResponse;
import com.inspire.msusuarios.model.usuarios.RolEquipo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {EquipoUsuarioMapper.class})
public interface RolEquipoMapper {
    RolEquipoMapper INSTANCE = Mappers.getMapper(RolEquipoMapper.class);

    RolResponse rolToRolResponse(RolEquipo rolEquipo);
}