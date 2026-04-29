$version: "2"

namespace com.github.keycloak

use com.github.common#BadRequestError
use com.github.common#InternalServerError
use com.github.common#UnauthorizedError

// ==================== GET CLIENTES ====================
@http(method: "GET", uri: "/v1/keycloak/clients", code: 200)
@readonly
@documentation("Obtiene lista de clientes de Keycloak del realm configurado.")
operation ListarKeycloakClients {
    input: ListarKeycloakClientsInput
    output: ListarKeycloakClientsOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure ListarKeycloakClientsInput {
    @required
    @httpQuery("max")
    max: Integer

    @required
    @httpQuery("first")
    first: Integer
}

structure ListarKeycloakClientsOutput {
    @required
    @httpPayload
    body: ClientsListResponse
}

structure ClientsListResponse {
    @required
    clients: ClientRepresentationList
}

list ClientRepresentationList {
    member: ClientRepresentation
}

structure ClientRepresentation {
    @required
    id: String

    @required
    clientId: String

    name: String

    description: String

    baseUrl: String

    protocol: String

    enabled: Boolean

    publicClient: Boolean

    directAccessGrantsEnabled: Boolean
}
