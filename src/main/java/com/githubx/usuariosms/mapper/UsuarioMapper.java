package com.githubx.usuariosms.mapper;

import com.githubx.usuariosms.dto.response.UsuarioResponse;
import com.githubx.usuariosms.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioResponse usuarioToUsuarioResponse(Usuario usuario);
}
