$version: "2"

namespace com.github.keycloak

use aws.protocols#restJson1

@title("Ms-Usuarios - Keycloak Management API")
@restJson1
@httpBearerAuth
@documentation("Microservicio de gestión de clientes, roles y permisos de Keycloak.")
service KeycloakManagementApi {
    version: "1.0.0"
    operations: [
        ListarKeycloakClients
        ListarRolesRealm
        ListarRolesCliente
        ObtenerRol
        ListarPermisosCliente
        ListarPermisosClienteRol
        ActualizarPermiso
    ]
}
