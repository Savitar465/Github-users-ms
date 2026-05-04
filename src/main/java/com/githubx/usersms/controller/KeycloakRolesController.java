package com.githubx.usersms.controller;

import com.githubx.usersms.service.contratos.KeycloakRolesService;
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
 * Controller that manages operations related to roles in Keycloak.
 */
@RestController
@RequestMapping("v1/keycloak/roles")
public class KeycloakRolesController {

    private final KeycloakRolesService keycloakRolesService;

    public KeycloakRolesController(KeycloakRolesService keycloakRolesService) {
        this.keycloakRolesService = keycloakRolesService;
    }


    /**
     * Endpoint to get a list of roles in Keycloak of the configured realm.
     *
     * @param max   Maximum number of roles to return
     * @param first Index of the first role to return
     * @return ResponseEntity containing the list of roles and HTTP status 200
     */
    @Operation(summary = "Gets list of roles from the realm",
            description = "Gets the list of roles in Keycloak according to the configured realm",
            parameters = {
                    @Parameter(name = "max", description = "Maximum number of roles to return", required = true),
                    @Parameter(name = "first", description = "Index of the first role to return", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Showing roles from the realm")
            }
    )
    @GetMapping(value = "/realm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleRepresentation>> getRealmRoles(@QueryParam("max") Integer max, @QueryParam("first") Integer first) {
        return new ResponseEntity<>(keycloakRolesService.getRealmRoles(max, first), HttpStatus.OK);
    }

    /**
     * Gets a list of roles associated with a specific client in Keycloak.
     *
     * @param clientUuid UUID of the client whose roles you want to get
     * @param max        Maximum number of roles to return
     * @param first      Index of the first role to return
     * @return ResponseEntity containing a list of RoleRepresentation objects representing the client's roles
     * and HTTP status 200
     */
    @Operation(summary = "Gets list of client roles",
            description = "Gets the list of roles in Keycloak according to the client",
            parameters = {
                    @Parameter(name = "clientUuid", description = "Client UUID", required = true),
                    @Parameter(name = "max", description = "Maximum number of roles to return", required = true),
                    @Parameter(name = "first", description = "Index of the first role to return", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Showing client roles")
            }
    )
    @GetMapping(value = "/client/{clientUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleRepresentation>> getClientRoles(@PathVariable("clientUuid") String clientUuid, @QueryParam("max") Integer max, @QueryParam("first") Integer first) {
        return new ResponseEntity<>(keycloakRolesService.getClientRoles(clientUuid, max, first), HttpStatus.OK);
    }


    /**
     * Gets a specific role from Keycloak by its ID.
     *
     * @param rolId ID of the role to get
     * @return ResponseEntity containing the RoleRepresentation object of the requested role and HTTP status 200
     */
    @Operation(summary = "Gets client role",
            description = "Gets a specific role from Keycloak by its ID",
            parameters = {
                    @Parameter(name = "rolId", description = "ID of the role to get", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Showing client role")
            }
    )
    @GetMapping(value = "/{rolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleRepresentation> getRole(@PathVariable("rolId") String rolId) {
        RoleRepresentation roleRepresentation = keycloakRolesService.getRole(rolId);
        return new ResponseEntity<>(roleRepresentation, HttpStatus.OK);
    }
}
