package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.AreaResponse;
import com.inspire.msusuarios.model.usuarios.Area;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AreaMapper {
    AreaMapper INSTANCE = Mappers.getMapper(AreaMapper.class);

    AreaResponse areaToAreaResponse(Area area);
}
