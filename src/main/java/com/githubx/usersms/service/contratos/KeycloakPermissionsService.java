package com.githubx.usersms.service.contratos;

import com.githubx.usersms.dto.request.RolLinkRequest;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;

import java.util.List;

public interface KeycloakPermissionsService {

    /**
     * Method that gets a list of client permissions
     *
     * @param clientUuid Client UUID
     * @param max        Maximum number of permissions to return
     * @param first      Index of the first permission to return
     * @return list of client permissions
     */
    List<PolicyRepresentation> getPermissions(String clientUuid, Integer max, Integer first);

    /**
     * Method that gets a list of permissions linked to a specific role.
     *
     * @param clientUuid Client UUID.
     * @param rolId      Role identifier.
     * @return list of permissions linked to the role.
     */
    List<PolicyRepresentation> getPermissionsByRole(String clientUuid, String rolId);

    /**
     * Method that updates the permissions of a client by assigning or adjusting the associated roles.
     *
     * @param clientUuid   the unique identifier of the client.
     * @param permissionId the unique identifier of the permission to update.
     * @param roles        the list of identifiers of the roles to associate with the permission.
     */
    void updatePermission(String clientUuid, String permissionId, List<RolLinkRequest> roles);

}
