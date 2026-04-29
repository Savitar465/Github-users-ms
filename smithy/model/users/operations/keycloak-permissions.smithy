$version: "2"

namespace com.github.keycloak

use com.github.common#BadRequestError
use com.github.common#InternalServerError
use com.github.common#NotFoundError
use com.github.common#StringList
use com.github.common#UnauthorizedError

// ==================== GET PERMISOS DEL CLIENTE ====================
@http(method: "GET", uri: "/v1/keycloak/permissions/client/{clientUuid}", code: 200)
@readonly
@documentation("Obtiene lista de permisos del cliente en Keycloak.")
operation ListarPermisosCliente {
    input: ListarPermisosClienteInput
    output: ListarPermisosClienteOutput
    errors: [
        BadRequestError
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure ListarPermisosClienteInput {
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

structure ListarPermisosClienteOutput {
    @required
    @httpPayload
    body: PermisosListResponse
}

structure PermisosListResponse {
    @required
    permisos: PolicyRepresentationList
}

// ==================== GET PERMISOS POR CLIENTE Y ROL ====================
@http(method: "GET", uri: "/v1/keycloak/permissions/client/{clientUuid}/rol/{rolId}", code: 200)
@readonly
@documentation("Obtiene permisos asociados a un cliente y rol específico.")
operation ListarPermisosClienteRol {
    input: ListarPermisosClienteRolInput
    output: ListarPermisosClienteRolOutput
    errors: [
        BadRequestError
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure ListarPermisosClienteRolInput {
    @required
    @httpLabel
    clientUuid: String

    @required
    @httpLabel
    rolId: String
}

structure ListarPermisosClienteRolOutput {
    @required
    @httpPayload
    body: PermisosListResponse
}

// ==================== PUT ACTUALIZAR PERMISO ====================
@http(method: "PUT", uri: "/v1/keycloak/permissions/client/{clientUuid}/permission/{permissionId}", code: 204)
@idempotent
@documentation("Actualiza un permiso específico del cliente.")
operation ActualizarPermiso {
    input: ActualizarPermisoInput
    output: Unit
    errors: [
        BadRequestError
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure ActualizarPermisoInput {
    @required
    @httpLabel
    clientUuid: String

    @required
    @httpLabel
    permissionId: String

    @required
    @httpPayload
    body: RolesUpdateRequest
}

structure RolesUpdateRequest {
    @required
    roles: RolLinkRequestList
}

// ==================== SHARED TYPES ====================
list PolicyRepresentationList {
    member: PolicyRepresentation
}

structure PolicyRepresentation {
    @required
    id: String

    @required
    name: String

    description: String

    type: String

    resources: StringList

    roles: StringList
}

list RolLinkRequestList {
    member: RolLinkRequest
}

structure RolLinkRequest {
    @required
    rolId: String

    @required
    rolNombre: String
}
