package com.inspire.msusuarios.service.implementacion;

import com.inspire.msusuarios.service.contratos.KeycloakClientsService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KeycloakClientsServiceImpl implements KeycloakClientsService {

    private final Keycloak keycloak;
    @Value("${keycloak-client.realm}")
    String realm;

    public KeycloakClientsServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public List<ClientRepresentation> obtenerClients(Integer max, Integer first) {
        return keycloak.realm(realm)
                .clients().findAll("", false, false, first, max);
    }
}
