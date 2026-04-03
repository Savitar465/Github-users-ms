package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.EquipoUsuarioResponse;
import com.inspire.msusuarios.model.usuarios.CargoArea;
import com.inspire.msusuarios.model.usuarios.EquipoUsuario;
import com.inspire.msusuarios.model.usuarios.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CargoArea.class, EquipoMapper.class, Usuario.class})
public interface EquipoUsuarioMapper {
    EquipoUsuarioMapper INSTANCE = Mappers.getMapper(EquipoUsuarioMapper.class);

    EquipoUsuarioResponse equipoUsuarioToEquipoUsuarioResponse(EquipoUsuario equipoUsuario);

}
