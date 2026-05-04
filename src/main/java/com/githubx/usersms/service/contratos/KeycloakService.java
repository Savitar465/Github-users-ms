package com.githubx.usersms.service.contratos;

import com.github.g.users.server.users.model.DeleteResponse;
import com.githubx.usersms.dto.request.RolRequest;
import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.dto.request.UserRolRequest;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.List;

public interface KeycloakService {
    /**
     * Method that obtains a list of users from Keycloak
     *
     * @return returns a list of users
     */
    List<UserRepresentation> findAllUsers();

    /**
     * Method that searches for a user by username
     *
     * @param username username of the user
     * @return returns the user with the username from the parameter
     */
    List<UserRepresentation> searchUserByUsername(String username);

    /**
     * Method that creates a user in Keycloak
     *
     * @param userDTO user to save
     * @return returns the created user with its respective Id
     */
    UserRepresentation createKeycloakUser(UserRequest userDTO);

    /**
     * Method assigns roles to users by their id
     *
     * @param userId id of the users to add roles
     * @param roles list of roles to add
     */
    void assignKeycloakRoles(String[] userId, List<RolRequest> roles);

    /**
     * Method deletes a user by its id
     *
     * @param userId id of the user to delete
     */
    DeleteResponse deleteUserKeycloak(String userId);

    /**
     * Method that edits a user by its id
     *
     * @param userId  id of the user to edit
     * @param userDTO user that contains the data to update
     * @return UserRepresentation object
     */
    UserRepresentation updateUserKeycloak(String userId, UserRolRequest userDTO, String username);

    /**
     * Method that edits a user by its id and also adds roles
     *
     * @param userId  id of the user to edit
     * @param userDTO user that contains the data to update
     * @return UserRepresentation object
     */
    UserRepresentation updateUserKeycloakAdmin(String userId, List<RolRequest> roles,
                                               UserRolRequest userDTO, String username);

    /**
     * Method that obtains the roles of a user
     *
     * @param userId id of the user whose roles are obtained
     * @return returns the list of roles obtained
     */
    List<RoleRepresentation> getUserRoles(String userId);
}