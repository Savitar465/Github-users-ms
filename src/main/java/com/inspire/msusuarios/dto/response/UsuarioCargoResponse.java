package com.inspire.msusuarios.dto.response;

import com.inspire.mscommon.dto.response.UsuarioResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioCargoResponse {
    private UsuarioResponse usuarioResponse;
}
