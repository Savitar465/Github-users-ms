package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.SistemaResponse;
import com.inspire.msusuarios.model.usuarios.Sistema;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface SistemaMapper {
    SistemaMapper INSTANCE = Mappers.getMapper(SistemaMapper.class);

    SistemaResponse sistemaToSistemaResponse(Sistema sistema);
}
