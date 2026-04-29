$version: "2"

namespace com.github.keycloak

use com.github.common#BadRequestError
use com.github.common#InternalServerError
use com.github.common#NotFoundError
use com.github.common#StringMap
use com.github.common#UnauthorizedError

// ==================== GET ROLES DEL REALM ====================
@http(method: "GET", uri: "/v1/keycloak/roles/realm", code: 200)
@readonly
@documentation("Obtiene lista de roles del realm configurado en Keycloak.")
operation ListarRolesRealm {
    input: ListarRolesRealmInput
    output: ListarRolesRealmOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure ListarRolesRealmInput {
    @required
    @httpQuery("max")
    max: Integer

    @required
    @httpQuery("first")
    first: Integer
}

structure ListarRolesRealmOutput {
    @required
    @httpPayload
    body: RolesListResponse
}

structure RolesListResponse {
    @required
    roles: RoleRepresentationList
}

// ==================== GET ROLES DEL CLIENTE ====================
@http(method: "GET", uri: "/v1/keycloak/roles/client/{clientUuid}", code: 200)
@readonly
@documentation("Obtiene lista de roles asociados a un cliente específico.")
operation ListarRolesCliente {
    input: ListarRolesClienteInput
    output: ListarRolesClienteOutput
    errors: [
        BadRequestError
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure ListarRolesClienteInput {
    @required
    @httpLabel
    clientUuid: String

    @required
    @httpQuery("max")
    max: Integer

    @required
    @httpQuery("first")
    first: Integer
}

structure ListarRolesClienteOutput {
    @required
    @httpPayload
    body: RolesListResponse
}

// ==================== GET ROL POR ID ====================
@http(method: "GET", uri: "/v1/keycloak/roles/{rolId}", code: 200)
@readonly
@documentation("Obtiene un rol específico de Keycloak por su ID.")
operation ObtenerRol {
    input: ObtenerRolInput
    output: ObtenerRolOutput
    errors: [
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure ObtenerRolInput {
    @required
    @httpLabel
    rolId: String
}

structure ObtenerRolOutput {
    @required
    @httpPayload
    body: RoleRepresentation
}

// ==================== SHARED TYPES ====================
list RoleRepresentationList {
    member: RoleRepresentation
}

structure RoleRepresentation {
    @required
    id: String

    @required
    name: String

    description: String

    composite: Boolean

    clientRole: Boolean

    containerId: String

    attributes: StringMap
}
