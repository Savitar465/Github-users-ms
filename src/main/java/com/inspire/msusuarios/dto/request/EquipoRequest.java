package com.inspire.msusuarios.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipoRequest {
    private String nombre;
    private String descripcion;
    private String tipo;
    private String equipoPadreId;
}
