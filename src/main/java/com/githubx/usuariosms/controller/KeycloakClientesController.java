package com.githubx.usuariosms.controller;

import com.githubx.usuariosms.service.contratos.KeycloakClientsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.QueryParam;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para gestionar operaciones relacionadas con clientes en Keycloak.
 * Expone endpoints RESTful para interactuar con el servicio de gestión de clientes.
 */
@RestController
@RequestMapping("v1/keycloak/clients")
public class KeycloakClientesController {

    private final KeycloakClientsService keycloakClientsService;

    public KeycloakClientesController(KeycloakClientsService keycloakClientsService) {
        this.keycloakClientsService = keycloakClientsService;
    }

    /**
     * Endpoint para obtener una lista de clientes en Keycloak.
     *
     * @param max   Número máximo de clientes a devolver.
     * @param first Índice del primer cliente a devolver.
     * @return ResponseEntity que contiene la lista de clientes y un código de estado HTTP 200.
     */
    @Operation(summary = "Obtiene lista de clientes",
            description = "Obtiente la lista de clientes en Keycloak según el realm configurado",
            parameters = {
                    @Parameter(name = "max", description = "Número máximo de clientes a devolver", required = true),
                    @Parameter(name = "first", description = "Índice del primer cliente a devolver", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mostrando clientes"),
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientRepresentation>> getListaClients(@QueryParam("max") Integer max, @QueryParam("first") Integer first) {
        List<ClientRepresentation> clients = keycloakClientsService.obtenerClients(max, first);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

}