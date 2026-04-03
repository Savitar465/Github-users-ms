package com.inspire.msusuarios.dto.response;

import lombok.Data;

@Data
public class CargoAreaResponse {
    private String cargoId;
    private String nombre;
    private String descripcion;
    private Integer estado;
    private String usuCre;
    private String fecCre;
    private String usuMod;
    private String fecMod;
    private AreaResponse areasAreaId;
}
