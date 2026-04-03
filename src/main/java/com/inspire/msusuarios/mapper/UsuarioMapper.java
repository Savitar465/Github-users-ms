package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.UsuarioResponse;
import com.inspire.msusuarios.model.usuarios.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AreaMapper.class})
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioResponse usuarioToUsuarioResponse(Usuario usuario);
}
