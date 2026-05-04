package com.githubx.usersms.service.contratos;

import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public interface KeycloakRolesService {

    /**
     * Method that gets a list of roles from the realm.
     *
     * @param max   maximum quantity of records to return
     * @param first initial position from where it should start listing
     * @return a list of RoleRepresentation objects representing the realm roles
     */
    List<RoleRepresentation> getRealmRoles(Integer max, Integer first);

    /**
     * Method that gets a list of roles from the client according to the client UUID.
     *
     * @param clientUuid Client UUID
     * @param max        maximum quantity of records to return
     * @param first      initial position from where it should start listing
     * @return a list of RoleRepresentation objects representing the client roles
     */
    List<RoleRepresentation> getClientRoles(String clientUuid, Integer max, Integer first);

    /**
     * Method that gets a specific role by its ID.
     *
     * @param rolId ID of the role to get
     * @return a RoleRepresentation object representing the requested role
     */
    RoleRepresentation getRole(String rolId);
}
