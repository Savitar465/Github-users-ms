package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.UsuariosSgsiResponse;
import com.inspire.msusuarios.model.sgsidb.UsuariosSgsi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioSgsiMapper {
    UsuarioSgsiMapper INSTANCE = Mappers.getMapper(UsuarioSgsiMapper.class);
    UsuariosSgsiResponse usersToUsersResponse(UsuariosSgsi usuariosSgsi);
}
