package com.inspire.msusuarios.service.contratos;

import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public interface KeycloakRolesService {

    /**
     * Método que obtiene una lista de roles del realm.
     *
     * @param max   cantidad máxima de registros a devolver
     * @param first posición inicial desde donde debe comenzar a listar
     * @return una lista de objetos de tipo RoleRepresentation que representan los roles del realm
     */
    List<RoleRepresentation> obtenerRolesRealm(Integer max, Integer first);

    /**
     * Método que obtiene una lista de roles del cliente según el UUID del cliente.
     *
     * @param clientUuid UUID del cliente
     * @param max        cantidad máxima de registros a devolver
     * @param first      posición inicial desde donde debe comenzar a listar
     * @return una lista de objetos de tipo RoleRepresentation que representan los roles del cliente
     */
    List<RoleRepresentation> obtenerRolesClient(String clientUuid, Integer max, Integer first);

    /**
     * Método que obtiene un rol específico por su ID.
     *
     * @param rolId ID del rol a obtener
     * @return un objeto de tipo RoleRepresentation que representa el rol solicitado
     */
    RoleRepresentation obtenerRol(String rolId);
}
