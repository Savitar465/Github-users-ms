$version: "2.0"

namespace com.github.common

@pattern("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$")
string Uuid

@length(min: 3, max: 50)
@pattern("^[a-zA-Z0-9._-]+$")
string Username

@length(min: 5, max: 255)
string Email

@length(min: 8, max: 128)
@sensitive
string Password

@sensitive
@length(min: 10, max: 2048)
string JwtToken

structure PaginationMeta {
    page: Integer
    perPage: Integer
    total: Integer
    totalPages: Integer
}

structure Identity {
    name: String
    email: Email
}

map StringMap {
    key: String
    value: String
}

list StringList {
    member: String
}

@error("client")
@httpError(400)
structure BadRequestError {
    @required
    message: String
}

@error("client")
@httpError(401)
structure UnauthorizedError {
    @required
    message: String
}

@error("client")
@httpError(403)
structure ForbiddenError {
    @required
    message: String
}

@error("client")
@httpError(404)
structure NotFoundError {
    @required
    message: String
}

@error("client")
@httpError(409)
structure ConflictError {
    @required
    message: String
}

@error("client")
@httpError(422)
structure UnprocessableEntityError {
    @required
    message: String
}

@error("server")
@httpError(500)
structure InternalServerError {
    @required
    message: String
}
