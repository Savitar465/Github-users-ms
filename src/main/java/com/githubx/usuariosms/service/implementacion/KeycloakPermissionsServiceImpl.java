package com.githubx.usuariosms.service.implementacion;

import com.githubx.usuariosms.dto.request.RolLinkRequest;
import com.githubx.usuariosms.service.contratos.KeycloakPermissionsService;
import com.githubx.usuariosms.util.errorhandling.exceptions.KeycloakException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ResourcePermissionsResource;
import org.keycloak.admin.client.resource.ScopePermissionsResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.keycloak.representations.idm.authorization.DecisionStrategy;
import org.keycloak.representations.idm.authorization.Logic;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourcePermissionRepresentation;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;
import org.keycloak.representations.idm.authorization.ScopePermissionRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakPermissionsServiceImpl implements KeycloakPermissionsService {

    private static final String ROLE_POLICY_TYPE = "roles";
    private final Keycloak keycloak;

    @Value("${keycloak-client.realm}")
    String realm;

    public KeycloakPermissionsServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Creates a representation of a role-based policy with the specified attributes.
     *
     * @param rolId       the unique identifier of the role in Keycloak.
     * @param name        the name of the policy.
     * @param description the description of the policy.
     * @return {@code RolePolicyRepresentation} instance representing the configured role policy.
     */
    private static RolePolicyRepresentation getRolePolicyRepresentation(String rolId, String name, String description) {
        RolePolicyRepresentation rolePolicyRepresentation = new RolePolicyRepresentation();
        rolePolicyRepresentation.setName(name);
        rolePolicyRepresentation.setDescription(description);
        rolePolicyRepresentation.setType("role");
        rolePolicyRepresentation.setDecisionStrategy(DecisionStrategy.UNANIMOUS);
        rolePolicyRepresentation.setLogic(Logic.POSITIVE);
        rolePolicyRepresentation.setFetchRoles(false);
        rolePolicyRepresentation.setRoles(Set.of(new RolePolicyRepresentation.RoleDefinition(rolId, false)));
        return rolePolicyRepresentation;
    }

    @Override
    public List<PolicyRepresentation> obtenerPermissions(String clientUuid, Integer max, Integer first) {
        return keycloak.realm(realm).clients().get(clientUuid).authorization().policies()
                .policies(null, null, null, null, null, true, null, null, first, max);
    }

    @Override
    public List<PolicyRepresentation> obtenerPermissionsByRol(String clientUuid, String rolId) {
        List<PolicyRepresentation> policiesRelated = new ArrayList<>();
        List<PolicyRepresentation> policyRepresentations = keycloak.realm(realm).clients().get(clientUuid).authorization()
                .policies().policies(null, null, null, null, null, false, null, null, null, null);
        PolicyRepresentation policy = policyRepresentations.stream()
                .filter(policyRepresentation -> policyRepresentation
                        .getConfig().get(ROLE_POLICY_TYPE) != null && policyRepresentation.getConfig().get(ROLE_POLICY_TYPE).contains(rolId))
                .toList().getFirst();
        if (policy != null) {
            policiesRelated.addAll(keycloak.realm(realm).clients().get(clientUuid).authorization().policies().policy(policy.getId()).dependentPolicies());
        }
        return policiesRelated;
    }

    @Override
    public void actualizarPermission(String clientUuid, String permissionId, List<RolLinkRequest> roles) {
        List<PolicyRepresentation> policyRepresentations = keycloak.realm(realm).clients().get(clientUuid).authorization()
                .policies().policies(null, null, null, null, null, false, null, null, null, null);
        List<RolLinkRequest> policiesExistentes = roles.stream().map(
                rolReq -> {
                    RoleRepresentation rol = keycloak.realm(realm).rolesById().getRole(rolReq.getRolId());
                    List<PolicyRepresentation> p = policyRepresentations.stream()
                            .filter(policyRepresentation -> policyRepresentation
                                    .getConfig().get(ROLE_POLICY_TYPE) != null && policyRepresentation.getConfig().get(ROLE_POLICY_TYPE).contains(rolReq.getRolId())).toList();
                    if (!p.isEmpty()) {
                        return new RolLinkRequest(p.getFirst().getId(), rolReq.isVincular());
                    } else {
                        RolePolicyRepresentation rolePolicyCreado = crearPolicy(rolReq.getRolId(), rol.getName() + "-rol",
                                "Policy para el rol " + rol.getName(), clientUuid);
                        return new RolLinkRequest(rolePolicyCreado.getId(), rolReq.isVincular());
                    }
                }
        ).toList().stream().filter(Objects::nonNull).toList();
        linkRolePolicyWithPermission(clientUuid, permissionId, policiesExistentes);
    }

    /**
     * Vincula una política de rol con un permiso en Keycloak actualizando las políticas asociadas del permiso especificado.
     *
     * @param clientUuid:         el identificador único del cliente en Keycloak.
     * @param permissionId:       el identificador único del permiso en Keycloak.
     * @param policiesExistentes: una lista de IDs de política que se asociarán con el permiso especificado.
     */
    private void linkRolePolicyWithPermission(String clientUuid, String permissionId, List<RolLinkRequest> policiesExistentes) {
        PolicyRepresentation permissionAsPolicy = keycloak.realm(realm).clients().get(clientUuid).authorization()
                .policies().policy(permissionId).toRepresentation();
        if (permissionAsPolicy.getType().equals("resource")) {
            ResourcePermissionsResource resourceR = keycloak.realm(realm).clients().get(clientUuid).authorization().permissions().resource();
            ResourcePermissionRepresentation rp = resourceR.findById(permissionId).toRepresentation();
            Set<String> policies = resourceR.findById(permissionId).associatedPolicies().stream().map(AbstractPolicyRepresentation::getId)
                    .filter(p -> policiesExistentes.stream().noneMatch(pR -> p.equals(pR.getRolId()) && !pR.isVincular()))
                    .collect(Collectors.toSet());
            policies.addAll(policiesExistentes.stream().map(p -> p.isVincular() ? p.getRolId() : null).filter(Objects::nonNull).collect(Collectors.toSet()));
            rp.setPolicies(policies);
            resourceR.findById(permissionId).update(rp);
        } else if (permissionAsPolicy.getType().equals("scope")) {
            ScopePermissionsResource resourceS = keycloak.realm(realm).clients().get(clientUuid).authorization().permissions().scope();
            ScopePermissionRepresentation rp = resourceS.findById(permissionId).toRepresentation();
            Set<String> policies = resourceS.findById(permissionId).associatedPolicies().stream().map(AbstractPolicyRepresentation::getId)
                    .filter(p -> policiesExistentes.stream().noneMatch(pR -> p.equals(pR.getRolId()) && !pR.isVincular()))
                    .collect(Collectors.toSet());
            policies.addAll(policiesExistentes.stream().map(p -> p.isVincular() ? p.getRolId() : null).filter(Objects::nonNull).collect(Collectors.toSet()));
            rp.setPolicies(policies);
            resourceS.findById(permissionId).update(rp);
        }
    }

    /**
     * Crea una política de tipo rol en Keycloak para un cliente específico utilizando
     * el rol, nombre y descripción proporcionados.
     *
     * @param rolId       el identificador único del rol en Keycloak.
     * @param name        el nombre de la política a crear.
     * @param description la descripción de la política.
     * @param clientUuid  el identificador único del cliente en Keycloak.
     * @return la representación de la política de rol creada.
     * @throws KeycloakException si ocurre un error durante la creación de la política
     *                           o si la política ya existe.
     */
    public RolePolicyRepresentation crearPolicy(String rolId, String name, String description, String clientUuid) {
        RolePolicyRepresentation rolePolicyRepresentation = getRolePolicyRepresentation(rolId, name, description);
        try (Response entity = keycloak.realm(realm).clients().get(clientUuid).authorization().policies().role().create(rolePolicyRepresentation)) {
            return entity.readEntity(RolePolicyRepresentation.class);
        } catch (RuntimeException e) {
            log.error("Error al crear la policy o ya existe la policy: {}", e.getMessage());
            throw new KeycloakException("Error al crear la policy o ya existe la policy: " + e.getMessage());
        }
    }

    //Falta la desvinculacion de permisos de las politicas-roles
}
