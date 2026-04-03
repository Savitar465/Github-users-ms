package com.inspire.msusuarios.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {
    private String clienteName;
    private String descripcion;
    private String tipo;
    private String sistemaId;
    private Map<String, Object> config;
}
