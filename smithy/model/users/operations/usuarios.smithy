$version: "2"

namespace com.github.users

use com.github.common#BadRequestError
use com.github.common#Email
use com.github.common#InternalServerError
use com.github.common#NotFoundError
use com.github.common#Password
use com.github.common#UnauthorizedError
use com.github.common#Username

@http(method: "GET", uri: "/v1/usuarios", code: 200)
@readonly
@documentation("Obtiene una lista paginada de usuarios registrados.")
operation ListarUsuarios {
    input: ListarUsuariosInput
    output: ListarUsuariosOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure ListarUsuariosInput {
    @required
    @httpQuery("pagina")
    pagina: Integer

    @required
    @httpQuery("cantidad")
    cantidad: Integer
}

structure ListarUsuariosOutput {
    @required
    @httpPayload
    body: ListarUsuariosResponseBody
}

structure ListarUsuariosResponseBody {
    @required
    usuarios: UsuarioResponseList

    @required
    totalElements: Long

    @required
    totalPages: Integer

    @required
    currentPage: Integer
}

@http(method: "GET", uri: "/v1/usuarios/{usuarioKyId}", code: 200)
@readonly
@documentation("Obtiene el detalle de un usuario por su identificador.")
operation ObtenerUsuario {
    input: ObtenerUsuarioInput
    output: ObtenerUsuarioOutput
    errors: [
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure ObtenerUsuarioInput {
    @required
    @httpLabel
    usuarioKyId: String
}

structure ObtenerUsuarioOutput {
    @required
    @httpPayload
    body: UsuarioResponse
}

@http(method: "POST", uri: "/v1/usuarios", code: 201)
@documentation("Crea un nuevo usuario en el sistema.")
operation CrearUsuario {
    input: CrearUsuarioInput
    output: CrearUsuarioOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure CrearUsuarioInput {
    @required
    @httpPayload
    body: UsuarioRequest
}

structure CrearUsuarioOutput {
    @required
    @httpPayload
    body: UsuarioResponse
}

@http(method: "PUT", uri: "/v1/usuarios/{usuarioKyId}", code: 200)
@idempotent
@documentation("Actualiza los datos de un usuario existente.")
operation ActualizarUsuario {
    input: ActualizarUsuarioInput
    output: ActualizarUsuarioOutput
    errors: [
        BadRequestError
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure ActualizarUsuarioInput {
    @required
    @httpLabel
    usuarioKyId: String

    @required
    @httpPayload
    body: UsuarioRequest
}

structure ActualizarUsuarioOutput {
    @required
    @httpPayload
    body: UsuarioResponse
}

@http(method: "DELETE", uri: "/v1/usuarios/{usuarioKyId}", code: 200)
@idempotent
@documentation("Elimina un usuario del sistema.")
operation EliminarUsuario {
    input: EliminarUsuarioInput
    output: EliminarUsuarioOutput
    errors: [
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure EliminarUsuarioInput {
    @required
    @httpLabel
    usuarioKyId: String
}

structure EliminarUsuarioOutput {
    @required
    @httpPayload
    body: EliminarResponse
}

@http(method: "POST", uri: "/v1/usuarios/search", code: 200)
@documentation("Busca usuarios aplicando filtros avanzados.")
operation BuscarUsuarios {
    input: BuscarUsuariosInput
    output: BuscarUsuariosOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure BuscarUsuariosInput {
    @required
    @httpPayload
    body: SearchRequest
}

structure BuscarUsuariosOutput {
    @required
    @httpPayload
    body: ListarUsuariosResponseBody
}

list UsuarioResponseList {
    member: UsuarioResponse
}

structure UsuarioRequest {
    @required
    nombres: String

    @required
    apellidos: String

    @required
    email: Email

    @required
    username: Username

    @required
    password: Password
}

structure UsuarioResponse {
    @required
    usuarioKyId: String

    @required
    email: String

    @required
    username: String

    @required
    nombres: String

    @required
    apellidos: String

    @required
    usuCre: String

    @required
    fecCre: String

    @required
    usuMod: String

    @required
    fecMod: String
}

structure EliminarResponse {
    @required
    mensaje: String

    @required
    exito: Boolean
}
