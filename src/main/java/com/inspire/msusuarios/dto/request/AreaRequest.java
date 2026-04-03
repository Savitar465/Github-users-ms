package com.inspire.msusuarios.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaRequest {
    private String nombre;
    private String descripcion;
}
