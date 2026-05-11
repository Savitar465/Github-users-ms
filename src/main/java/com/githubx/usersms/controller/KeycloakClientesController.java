package com.githubx.usersms.controller;

import com.githubx.usersms.service.contratos.KeycloakClientsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.QueryParam;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to manage operations related to clients in Keycloak.
 * Exposes RESTful endpoints to interact with the client management service.
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("v1/keycloak/clients")
public class KeycloakClientesController {

    private final KeycloakClientsService keycloakClientsService;

    public KeycloakClientesController(KeycloakClientsService keycloakClientsService) {
        this.keycloakClientsService = keycloakClientsService;
    }

    /**
     * Endpoint to get a list of clients in Keycloak.
     *
     * @param max   Maximum number of clients to return.
     * @param first Index of the first client to return.
     * @return ResponseEntity containing the list of clients and HTTP status 200.
     */
    @Operation(summary = "Gets list of clients",
            description = "Gets the list of clients in Keycloak according to the configured realm",
            parameters = {
                    @Parameter(name = "max", description = "Maximum number of clients to return", required = true),
                    @Parameter(name = "first", description = "Index of the first client to return", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Showing clients"),
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientRepresentation>> getClients(@QueryParam("max") Integer max, @QueryParam("first") Integer first) {
        List<ClientRepresentation> clients = keycloakClientsService.getClients(max, first);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

}