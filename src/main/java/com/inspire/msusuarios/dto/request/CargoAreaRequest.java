package com.inspire.msusuarios.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargoAreaRequest {
    @NotNull
    private String nombre;
    private String descripcion;
    @NotNull
    private String areasAreaId;
}
