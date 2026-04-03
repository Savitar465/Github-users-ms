package com.inspire.msusuarios.service.implementacion;

import com.inspire.msusuarios.dto.request.UsuarioRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.msusuarios.dto.request.RolRequest;
import com.inspire.msusuarios.dto.request.UsuarioRolRequest;
import com.inspire.msusuarios.service.contratos.KeycloakService;
import com.inspire.msusuarios.util.errorhandling.exceptions.KeycloakException;
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
    public UserRepresentation crearUsuarioKeycloak(@NonNull UsuarioRequest userDTO) {
        try (Response response = keycloak.realm(realm).users().create(userDTOToUserRepresentation(userDTO))) {
            if (response.getStatus() == HttpStatus.CREATED.value()) {
                String path = response.getLocation().getPath();
                String userId = path.substring(path.lastIndexOf("/") + 1);
                return keycloak.realm(realm).users().get(userId).toRepresentation();
            } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                log.error("Usuario ya existente!");
                throw new EntityConflictException("Usuario ya existente!");
            } else {
                log.error("Response |  Status: {} | Status Info: {}", response.getStatus(), response.getStatusInfo());
                throw new KeycloakException("Ocurrio un error al crear usuario, porfavor contacta con el administrador.");
            }
        }
    }

    public List<RoleRepresentation> obtenerRolesUsuario(String idUsuario) {
        UserResource userResource = keycloak.realm(realm).users().get(idUsuario);
        return userResource.roles().realmLevel().listEffective();
    }

    @Override
    public void asignarRolesKeycloak(String[] usuariosId, List<RolRequest> roles) {
        for (String userId : usuariosId) {
            List<RoleRepresentation> rolesRepresentation = keycloak.realm(realm).roles()
                    .list()
                    .stream()
                    .filter(role -> roles
                            .stream()
                            .anyMatch(roleName -> roleName.getNombre().equalsIgnoreCase(role.getName())))
                    .toList();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(rolesRepresentation);
        }
    }

    @Override
    public EliminarResponse deleteUserKeycloak(String userId) {
        keycloak.realm(realm).users()
                .get(userId)
                .remove();
        return new EliminarResponse(userId, "Se eliminó el usuario con id " + userId + " correctamente");
    }

    @Override
    public UserRepresentation updateUserKeycloak(String userId, @NonNull UsuarioRolRequest userDTO, String username) {
        UserResource usersResource = keycloak.realm(realm).users().get(obtenerUsuarioPorUsername(username).getId());
        UserRepresentation user = usersResource.toRepresentation();

        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getNombres());
        user.setLastName(userDTO.getApellidos());
        user.setEmail(userDTO.getEmail());

        user.singleAttribute("birthDate",
                new SimpleDateFormat("d/M/yy, H:mm").format(userDTO.getFechaNacimiento()));
        usersResource.update(user);
        return user;
    }

    @Override
    public UserRepresentation updateUserKeycloakAdmin(String userId,
                                                      List<RolRequest> roles, @NonNull UsuarioRolRequest userDTO, String username) {
        UserRepresentation user = updateUserKeycloak(userId, userDTO, username);
        asignarRolesKeycloak(new String[]{userId}, roles);
        return user;
    }
    
    private UserRepresentation userDTOToUserRepresentation(UsuarioRequest userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setFirstName(userDTO.getNombres());
        userRepresentation.setLastName(userDTO.getApellidos());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(userDTO.getPassword());
        userRepresentation.setCredentials(List.of(credentialRepresentation));

        return userRepresentation;
    }

    private UserRepresentation obtenerUsuarioPorUsername(String username) {
        List<UserRepresentation> usuarioR = keycloak.realm(realm).users().searchByUsername(username, true);
        if (!usuarioR.isEmpty()) {
            return usuarioR.getFirst();
        }
        log.error("No se encontró al usuario {} en Keycloak", username);
        return null;
    }

    public void adicionarAtributoId(String username, String id) {
        UserRepresentation usuarioR = obtenerUsuarioPorUsername(username);
        UserResource userResource = keycloak.realm(realm).users().get(usuarioR.getId());
        UserRepresentation user = userResource.toRepresentation();
        user.singleAttribute("user_db_id", id);
        userResource.update(user);
    }
}