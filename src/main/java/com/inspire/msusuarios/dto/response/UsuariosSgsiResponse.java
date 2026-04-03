package com.inspire.msusuarios.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class UsuariosSgsiResponse {
    private String nombres;
    private String apellidos;
    private String email;
    private String username;
    private String emailVerifiedAt;
    private String password;
    private String nombreCompleto;
    private String areaUnidad;
    private String rememberToken;
    private String permissions;
    private String avatar;
    private String updateAt;
    private String tipoDocumento;
    private String nroDocumento;
    private String tipoContrato;
    private String nroCelular;
    private Date fechaInicio;
    private Date fechaFin;
    private String emailPersonal;
    private String genero;
    private Integer estado;
    private Date createdAt;
    private Date updatedAt;
    private String ctlAccesos;
    private Date fechaNacimiento;
}
