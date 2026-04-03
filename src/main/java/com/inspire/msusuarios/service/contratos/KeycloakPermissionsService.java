package com.inspire.msusuarios.service.contratos;

import com.inspire.msusuarios.dto.request.RolLinkRequest;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;

import java.util.List;

public interface KeycloakPermissionsService {

    /**
     * Método que obtiene una lista de permisos del cliente
     *
     * @param clientUuid UUID del cliente
     * @param max        Número máximo de permisos a devolver
     * @param first      Índice del primer permiso a devolver
     * @return lista de permisos del cliente
     */
    List<PolicyRepresentation> obtenerPermissions(String clientUuid, Integer max, Integer first);

    /**
     * Metodo que obtiene una lista de permisos vinculados a un rol específico.
     *
     * @param clientUuid UUID del cliente.
     * @param rolId      Identificador del rol.
     * @return lista de permisos vinculados al rol.
     */
    List<PolicyRepresentation> obtenerPermissionsByRol(String clientUuid, String rolId);

    /**
     * Metodo que actualiza los permisos de un cliente asignando o ajustando los roles asociados.
     *
     * @param clientUuid   el identificador único del cliente.
     * @param permissionId el identificador único del permiso a actualizar.
     * @param roles        la lista de identificadores de los roles a asociar con el permiso.
     */
    void actualizarPermission(String clientUuid, String permissionId, List<RolLinkRequest> roles);

}
