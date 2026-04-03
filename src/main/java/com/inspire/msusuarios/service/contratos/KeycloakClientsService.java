package com.inspire.msusuarios.service.contratos;

import org.keycloak.representations.idm.ClientRepresentation;

import java.util.List;

public interface KeycloakClientsService {

    /**
     * Método para obtener los clientes de Keycloak del realm configurado
     *
     * @return lista de clientes del realm
     */
    List<ClientRepresentation> obtenerClients(Integer max, Integer first);
}
