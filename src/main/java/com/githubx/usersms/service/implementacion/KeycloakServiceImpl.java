package com.githubx.usersms.service.implementacion;

import com.github.g.users.server.users.model.DeleteResponse;
import com.githubx.usersms.dto.request.RolRequest;
import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.util.errorhandling.exceptions.EntityConflictException;
import com.githubx.usersms.dto.request.UserRolRequest;
import com.githubx.usersms.service.contratos.KeycloakService;
import com.githubx.usersms.util.errorhandling.exceptions.KeycloakException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloak;
    @Value("${keycloak-client.realm}")
    String realm;

    public KeycloakServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public List<UserRepresentation> findAllUsers() {
        return keycloak.realm(realm)
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return keycloak.realm(realm)
                .users()
                .searchByUsername(username, true);
    }

    @Override
    public UserRepresentation createKeycloakUser(@NonNull UserRequest userDTO) {
        try (Response response = keycloak.realm(realm).users().create(userDTOToUserRepresentation(userDTO))) {
            if (response.getStatus() == HttpStatus.CREATED.value()) {
                String path = response.getLocation().getPath();
                String userId = path.substring(path.lastIndexOf("/") + 1);
                return keycloak.realm(realm).users().get(userId).toRepresentation();
            } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                log.error("User already exists!");
                throw new EntityConflictException("User already exists!");
            } else {
                log.error("Response |  Status: {} | Status Info: {}", response.getStatus(), response.getStatusInfo());
                throw new KeycloakException("An error occurred while creating the user, please contact the administrator.");
            }
        }
    }

    public List<RoleRepresentation> getUserRoles(String userId) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        return userResource.roles().realmLevel().listEffective();
    }

    @Override
    public void assignKeycloakRoles(String[] userIds, List<RolRequest> roles) {
        for (String userId : userIds) {
            List<RoleRepresentation> rolesRepresentation = keycloak.realm(realm).roles()
                    .list()
                    .stream()
                    .filter(role -> roles
                            .stream()
                            .anyMatch(roleName -> roleName.getName().equalsIgnoreCase(role.getName())))
                    .toList();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(rolesRepresentation);
        }
    }

    @Override
    public DeleteResponse deleteUserKeycloak(String userId) {
        keycloak.realm(realm).users()
                .get(userId)
                .remove();
        return new DeleteResponse(userId, true);
    }

    @Override
    public UserRepresentation updateUserKeycloak(String userId, @NonNull UserRolRequest userDTO, String username) {
        UserResource usersResource = keycloak.realm(realm).users().get(getUserByUsernameInternal(username).getId());
        UserRepresentation user = usersResource.toRepresentation();

        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        user.singleAttribute("birthDate",
                new SimpleDateFormat("d/M/yy, H:mm").format(userDTO.getBirthDate()));
        usersResource.update(user);
        return user;
    }

    @Override
    public UserRepresentation updateUserKeycloakAdmin(String userId,
                                                      List<RolRequest> roles, @NonNull UserRolRequest userDTO, String username) {
        UserRepresentation user = updateUserKeycloak(userId, userDTO, username);
        assignKeycloakRoles(new String[]{userId}, roles);
        return user;
    }
    
    private UserRepresentation userDTOToUserRepresentation(UserRequest userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(userDTO.getPassword());
        userRepresentation.setCredentials(List.of(credentialRepresentation));

        return userRepresentation;
    }

    private UserRepresentation getUserByUsernameInternal(String username) {
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().searchByUsername(username, true);
        if (!userRepresentations.isEmpty()) {
            return userRepresentations.getFirst();
        }
        log.error("User {} not found in Keycloak", username);
        return null;
    }

}