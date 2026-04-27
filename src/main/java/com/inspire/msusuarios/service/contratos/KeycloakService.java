package com.inspire.msusuarios.service.contratos;

import com.inspire.msusuarios.dto.request.RolRequest;
import com.inspire.msusuarios.dto.request.UsuarioRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.msusuarios.dto.request.UsuarioRolRequest;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.List;

public interface KeycloakService {
    /**
     * Método que obtiene una lista de usuarios de Keycloak
     *
     * @return retorna una lista de usuarios
     */
    List<UserRepresentation> findAllUsers();

    /**
     * Método que busca un usuario por su username
     *
     * @param username username del usuario
     * @return retorna el usuario con el username del parámetro
     */
    List<UserRepresentation> searchUserByUsername(String username);

    /**
     * Método que crea un usuario en Keycloak
     *
     * @param userDTO usuario a guardar
     * @return retorna el usuario creado con su respectiva Id
     */
    UserRepresentation crearUsuarioKeycloak(UsuarioRequest userDTO);

    /**
     * Método asigna roles a los usuarios por su id
     *
     * @param userId      id de los usuarios a adicionar roles
     * @param roles lista de roles a adicionar
     */
    void asignarRolesKeycloak(String[] userId, List<RolRequest> roles);

    /**
     * Método elimina un usuario por su id
     *
     * @param userId id del usuario a eliminar
     */
    EliminarResponse deleteUserKeycloak(String userId);

    /**
     * Método que edita un usuario por su id
     *
     * @param userId  id del usuario a editar
     * @param userDTO usuario que contiene los datos a actualizar
     *   * @return objeto tipo userRepresentation
     */
    UserRepresentation updateUserKeycloak(String userId, UsuarioRolRequest userDTO, String username);

     /**
     * Método que edita un usuario por su id y también añade roles
     *
     * @param userId  id del usuario a editar
     * @param userDTO usuario que contiene los datos a actualizar
     * @return objeto tipo userRepresentation
     */
    UserRepresentation updateUserKeycloakAdmin(String userId, List<RolRequest> roles,
                                               UsuarioRolRequest userDTO, String username);

     /**
     * Método que obtiene los roles de un usuario
     *
     * @param userId  id del usuario del que se obtiene los roles
     * @return devuelve la lista de roles obtenidos
     */
    List<RoleRepresentation> obtenerRolesUsuario(String userId);
}