package com.githubx.usuariosms.controller;

import com.githubx.usuariosms.dto.request.RolLinkRequest;
import com.githubx.usuariosms.service.contratos.KeycloakPermissionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.QueryParam;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador que gestiona operaciones relacionadas con los permisos en Keycloak.
 */
@RestController
@RequestMapping("v1/keycloak/permissions")
public class KeycloakPermissionsController {
    private final KeycloakPermissionsService keycloakPermissionsService;

    public KeycloakPermissionsController(KeycloakPermissionsService keycloakPermissionsService) {
        this.keycloakPermissionsService = keycloakPermissionsService;
    }

    /**
     * Endpoint para obtener una lista de permisos en Keycloak del real configurado.
     *
     * @param clientUuid UUID del cliente
     * @param max        Número máximo de permisos a devolver
     * @param first      Índice del primer permiso a devolver
     * @return ResponseEntity que contiene la lista de permisos y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtiene lista de permisos del cliente",
            description = "Obtiente la lista de permisos en Keycloak segun el cliente",
            parameters = {
                    @Parameter(name = "clientUuid", description = "UUID del cliente", required = true),
                    @Parameter(name = "max", description = "Número máximo de permisos a devolver", required = true),
                    @Parameter(name = "first", description = "Índice del primer permiso a devolver", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mostrando permisos del cliente")
            }
    )
    @GetMapping(value = "client/{clientUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PolicyRepresentation>> getListaPermissions(@PathVariable("clientUuid") String clientUuid,
                                                                          @QueryParam("max") Integer max,
                                                                          @QueryParam("first") Integer first) {
        return new ResponseEntity<>(keycloakPermissionsService.obtenerPermissions(clientUuid, max, first), HttpStatus.OK);
    }

    /**
     * Endpoint para obtener una lista de permisos vinculados a un rol específico.
     *
     * @param clientUuid UUID del cliente
     * @param rolId      ID del rol
     * @return ResponseEntity que contiene la lista de permisos y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtiene lista de permisos del cliente por rol",
            description = "Obtiene la lista de permisos en Keycloak segun el cliente y el rol",
            parameters = {
                    @Parameter(name = "clientUuid", description = "UUID del cliente", required = true),
                    @Parameter(name = "rolId", description = "ID del rol", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mostrando permisos del cliente por rol")
            }
    )
    @GetMapping(value = "client/{clientUuid}/rol/{rolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PolicyRepresentation>> getListaPermissionsByRol(@PathVariable("clientUuid") String clientUuid,
                                                                               @PathVariable("rolId") String rolId) {
        return new ResponseEntity<>(keycloakPermissionsService.obtenerPermissionsByRol(clientUuid, rolId), HttpStatus.OK);
    }

    /**
     * Actualiza un permiso específico para un cliente.
     *
     * @param clientUuid   UUID del cliente cuyo permiso necesita ser actualizado.
     * @param permissionId ID del permiso que será actualizado.
     * @param roles        La nueva representación del permiso.
     * @return ResponseEntity que contiene el estado HTTP de la operación (HTTP 200 si fue exitoso).
     */
    @Operation(
            summary = "Modifica un permiso del cliente",
            description = "Modifica un permiso del cliente",
            parameters = {
                    @Parameter(name = "clientUuid", description = "UUID del cliente", required = true),
                    @Parameter(name = "permissionId", description = "ID del permiso", required = true),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mostrando permisos del cliente")
            }
    )
    @PutMapping(value = "/client/{clientUuid}/permission/{permissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> putPermission(@PathVariable("clientUuid") String clientUuid,
                                                @PathVariable("permissionId") String permissionId,
                                                @RequestBody List<RolLinkRequest> roles) {
        keycloakPermissionsService.actualizarPermission(clientUuid, permissionId, roles);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
