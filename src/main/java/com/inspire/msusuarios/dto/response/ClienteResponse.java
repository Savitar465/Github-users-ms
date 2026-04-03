package com.inspire.msusuarios.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ClienteResponse {
    private String clienteId;
    private String clienteName;
    private String tipo;
    private String usuCre;
    private Date fecCre;
    private String usuMod;
    private Date fecMod;
    private SistemaResponse sistemasSistemaId;
    private Map<String, Object> config;
}
