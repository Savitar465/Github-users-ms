package com.githubx.usersms.service.contratos;

import org.keycloak.representations.idm.ClientRepresentation;

import java.util.List;

public interface KeycloakClientsService {

    /**
     * Method to get the Keycloak clients from the configured realm
     *
     * @return list of realm clients
     */
    List<ClientRepresentation> getClients(Integer max, Integer first);
}
