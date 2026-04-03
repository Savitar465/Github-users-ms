package com.inspire.msusuarios.controller;

import com.inspire.msusuarios.service.contratos.KeycloakRolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.QueryParam;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador que gestiona operaciones relacionadas con los roles en Keycloak.
 */
@RestController
@RequestMapping("v1/keycloak/roles")
public class KeycloakRolesController {

    private final KeycloakRolesService keycloakRolesService;

    public KeycloakRolesController(KeycloakRolesService keycloakRolesService) {
        this.keycloakRolesService = keycloakRolesService;
    }


    /**
     * Endpoint para obtener una lista de roles en Keycloak del real configurado.
     *
     * @param max   Número máximo de roles a devolver
     * @param first Índice del primer rol a devolver
     * @return ResponseEntity que contiene la lista de roles y un código de estado HTTP 200
     */
    @Operation(summary = "Obtiene lista de roles del realm",
            description = "Obtiente la lista de roles en Keycloak segun el realm configurado",
            parameters = {
                    @Parameter(name = "max", description = "Número máximo de roles a devolver", required = true),
                    @Parameter(name = "first", description = "Índice del primer rol a devolver", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mostrando roles del realm")
            }
    )
    @GetMapping(value = "/realm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleRepresentation>> getRolesRealm(@QueryParam("max") Integer max, @QueryParam("first") Integer first) {
        return new ResponseEntity<>(keycloakRolesService.obtenerRolesRealm(max, first), HttpStatus.OK);
    }

    /**
     * Obtiene una lista de roles asociados a un cliente específico en Keycloak.
     *
     * @param clientUuid UUID del cliente cuyos roles se desean obtener
     * @param max        Número máximo de roles a devolver
     * @param first      Índice del primer rol a devolver
     * @return ResponseEntity que contiene una lista de objetos RoleRepresentation representando los roles del cliente
     * y un código de estado HTTP 200
     */
    @Operation(summary = "Obtiene lista de roles del cliente",
            description = "Obtiente la lista de roles en Keycloak segun el cliente",
            parameters = {
                    @Parameter(name = "clientUuid", description = "UUID del cliente", required = true),
                    @Parameter(name = "max", description = "Número máximo de roles a devolver", required = true),
                    @Parameter(name = "first", description = "Índice del primer rol a devolver", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mostrando roles del cliente")
            }
    )
    @GetMapping(value = "/client/{clientUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleRepresentation>> getRolesClient(@PathVariable("clientUuid") String clientUuid, @QueryParam("max") Integer max, @QueryParam("first") Integer first) {
        return new ResponseEntity<>(keycloakRolesService.obtenerRolesClient(clientUuid, max, first), HttpStatus.OK);
    }


    /**
     * Obtiene un rol específico de Keycloak por su ID.
     *
     * @param rolId ID del rol a obtener
     * @return ResponseEntity que contiene el objeto RoleRepresentation del rol solicitado y un código de estado HTTP 200
     */
    @Operation(summary = "Obtiene rol del cliente",
            description = "Obtiene un rol específico de Keycloak por su ID",
            parameters = {
                    @Parameter(name = "rolId", description = "ID del rol a obtener", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mostrando rol del cliente")
            }
    )
    @GetMapping(value = "/{rolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleRepresentation> getRol(@PathVariable("rolId") String rolId) {
        RoleRepresentation roleRepresentation = keycloakRolesService.obtenerRol(rolId);
        return new ResponseEntity<>(roleRepresentation, HttpStatus.OK);
    }
}
