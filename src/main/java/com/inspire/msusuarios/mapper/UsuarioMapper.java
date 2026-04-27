package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.UsuarioResponse;
import com.inspire.msusuarios.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioResponse usuarioToUsuarioResponse(Usuario usuario);
}
