package com.inspire.msusuarios.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SistemaRequest {
    private String nombre;
    private String descripcion;
}
