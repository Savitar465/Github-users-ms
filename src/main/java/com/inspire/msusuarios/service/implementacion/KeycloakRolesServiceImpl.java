package com.inspire.msusuarios.service.implementacion;

import com.inspire.msusuarios.service.contratos.KeycloakRolesService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KeycloakRolesServiceImpl implements KeycloakRolesService {

    private final Keycloak keycloak;
    @Value("${keycloak-client.realm}")
    String realm;

    public KeycloakRolesServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public List<RoleRepresentation> obtenerRolesRealm(Integer max, Integer first) {
        return keycloak.realm(realm).roles().list(first,max);
    }

    @Override
    public List<RoleRepresentation> obtenerRolesClient(String clientUuid, Integer max, Integer first) {
        return keycloak.realm(realm).clients().get(clientUuid).roles().list(first,max);
    }

    @Override
    public RoleRepresentation obtenerRol(String rolId) {
        return keycloak.realm(realm).rolesById().getRole(rolId);
    }

}
