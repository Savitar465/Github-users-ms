package com.inspire.msusuarios.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class SistemaResponse {
    private String sistemaId;
    private String nombre;
    private String descripcion;
    private String usuCre;
    private Date fecCre;
    private String usuMod;
    private Date fecMod;
}
