package com.inspire.msusuarios.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipoUsuariosResponse {
    private EquipoResponse equipoResponse;
    private List<UsuarioCargoResponse> usuarioCargoResponses;
}
