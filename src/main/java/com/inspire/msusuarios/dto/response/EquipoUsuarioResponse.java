package com.inspire.msusuarios.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EquipoUsuarioResponse {
    private String equipoUsuarioId;
    private RolResponse rol;
    private UsuarioResponse usuario;
    private EquipoResponse equipo;
}
