$version: "2"

namespace com.github.users

use aws.protocols#restJson1

@title("Ms-Usuarios - Users Management API")
@restJson1
@httpBearerAuth
@documentation("Microservicio de gestión de usuarios con integración a Keycloak para autenticación y autorización.")
service UsersApi {
    version: "1.0.0"
    operations: [
        ListarUsuarios
        ObtenerUsuario
        CrearUsuario
        ActualizarUsuario
        EliminarUsuario
        BuscarUsuarios
    ]
}
