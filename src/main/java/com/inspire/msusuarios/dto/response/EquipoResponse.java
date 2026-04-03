package com.inspire.msusuarios.dto.response;


import lombok.Data;


@Data
public class EquipoResponse {
    private String equipoId;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String fecCre;
    private String usuCre;
    private String fecMod;
    private String usuMod;
    private String pathId;
    private String equipoPadre;
}
