package com.inspire.msusuarios.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class UsuarioResponse {
    private String usuarioKyId;
    private String email;
    private String username;
    private String nombres;
    private String apellidos;
    private String usuCre;
    private Date fecCre;
    private String usuMod;
    private Date fecMod;
}