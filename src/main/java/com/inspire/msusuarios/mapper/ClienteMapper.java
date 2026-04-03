package com.inspire.msusuarios.mapper;

import com.inspire.msusuarios.dto.response.ClienteResponse;
import com.inspire.msusuarios.model.usuarios.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SistemaMapper.class})
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);
    ClienteResponse clienteToClienteResponse(Cliente cliente);
}
