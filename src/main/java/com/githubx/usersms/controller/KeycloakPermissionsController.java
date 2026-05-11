package com.githubx.usersms.controller;

import com.githubx.usersms.dto.request.RolLinkRequest;
import com.githubx.usersms.service.contratos.KeycloakPermissionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.QueryParam;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller that manages operations related to permissions in Keycloak.
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("v1/keycloak/permissions")
public class KeycloakPermissionsController {
    private final KeycloakPermissionsService keycloakPermissionsService;

    public KeycloakPermissionsController(KeycloakPermissionsService keycloakPermissionsService) {
        this.keycloakPermissionsService = keycloakPermissionsService;
    }

    /**
     * Endpoint to get a list of permissions in Keycloak of the configured realm.
     *
     * @param clientUuid UUID of the client
     * @param max        Maximum number of permissions to return
     * @param first      Index of the first permission to return
     * @return ResponseEntity containing the list of permissions and HTTP status 200
     */
    @Operation(
            summary = "Gets list of client permissions",
            description = "Gets the list of permissions in Keycloak according to the client",
            parameters = {
                    @Parameter(name = "clientUuid", description = "Client UUID", required = true),
                    @Parameter(name = "max", description = "Maximum number of permissions to return", required = true),
                    @Parameter(name = "first", description = "Index of the first permission to return", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Showing client permissions")
            }
    )
    @GetMapping(value = "client/{clientUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PolicyRepresentation>> getPermissions(@PathVariable("clientUuid") String clientUuid,
                                                                     @QueryParam("max") Integer max,
                                                                     @QueryParam("first") Integer first) {
        return new ResponseEntity<>(keycloakPermissionsService.getPermissions(clientUuid, max, first), HttpStatus.OK);
    }

    /**
     * Endpoint to get a list of permissions linked to a specific role.
     *
     * @param clientUuid UUID of the client
     * @param rolId      ID of the role
     * @return ResponseEntity containing the list of permissions and HTTP status 200
     */
    @Operation(
            summary = "Gets list of client permissions by role",
            description = "Gets the list of permissions in Keycloak according to the client and the role",
            parameters = {
                    @Parameter(name = "clientUuid", description = "Client UUID", required = true),
                    @Parameter(name = "rolId", description = "Role ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Showing client permissions by role")
            }
    )
    @GetMapping(value = "client/{clientUuid}/rol/{rolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PolicyRepresentation>> getPermissionsByRole(@PathVariable("clientUuid") String clientUuid,
                                                                           @PathVariable("rolId") String rolId) {
        return new ResponseEntity<>(keycloakPermissionsService.getPermissionsByRole(clientUuid, rolId), HttpStatus.OK);
    }

    /**
     * Updates a specific permission for a client.
     *
     * @param clientUuid   UUID of the client whose permission needs to be updated.
     * @param permissionId ID of the permission that will be updated.
     * @param roles        The new permission representation.
     * @return ResponseEntity containing the HTTP status of the operation (HTTP 200 if successful).
     */
    @Operation(
            summary = "Modifies a client permission",
            description = "Modifies a client permission",
            parameters = {
                    @Parameter(name = "clientUuid", description = "Client UUID", required = true),
                    @Parameter(name = "permissionId", description = "Permission ID", required = true),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Showing client permissions")
            }
    )
    @PutMapping(value = "/client/{clientUuid}/permission/{permissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePermission(@PathVariable("clientUuid") String clientUuid,
                                                   @PathVariable("permissionId") String permissionId,
                                                   @RequestBody List<RolLinkRequest> roles) {
        keycloakPermissionsService.updatePermission(clientUuid, permissionId, roles);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
