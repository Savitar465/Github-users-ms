$version: "2"

namespace com.github.users

use com.github.common#BadRequestError
use com.github.common#Email
use com.github.common#InternalServerError
use com.github.common#NotFoundError
use com.github.common#Password
use com.github.common#UnauthorizedError
use com.github.common#Username

@http(method: "GET", uri: "/v1/users", code: 200)
@readonly
@documentation("Gets a paginated list of registered users.")
operation ListUsers {
    input: ListUsersInput
    output: ListUsersOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure ListUsersInput {
    @required
    @httpQuery("page")
    page: Integer

    @required
    @httpQuery("pageSize")
    pageSize: Integer
}

structure ListUsersOutput {
    @required
    @httpPayload
    body: ListUsersResponseBody
}

structure ListUsersResponseBody {
    @required
    users: UserResponseList

    @required
    totalElements: Long

    @required
    totalPages: Integer

    @required
    currentPage: Integer
}

@http(method: "GET", uri: "/v1/users/{userId}", code: 200)
@readonly
@documentation("Gets the details of a user by their identifier.")
operation GetUser {
    input: GetUserInput
    output: GetUserOutput
    errors: [
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure GetUserInput {
    @required
    @httpLabel
    userId: String
}

structure GetUserOutput {
    @required
    @httpPayload
    body: UserResponse
}

@http(method: "POST", uri: "/v1/users", code: 201)
@documentation("Creates a new user in the system.")
operation CreateUser {
    input: CreateUserInput
    output: CreateUserOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure CreateUserInput {
    @required
    @httpPayload
    body: UserRequest
}

structure CreateUserOutput {
    @required
    @httpPayload
    body: UserResponse
}

@http(method: "PUT", uri: "/v1/users/{userId}", code: 200)
@idempotent
@documentation("Updates the data of an existing user.")
operation UpdateUser {
    input: UpdateUserInput
    output: UpdateUserOutput
    errors: [
        BadRequestError
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure UpdateUserInput {
    @required
    @httpLabel
    userId: String

    @required
    @httpPayload
    body: UserRequest
}

structure UpdateUserOutput {
    @required
    @httpPayload
    body: UserResponse
}

@http(method: "DELETE", uri: "/v1/users/{userId}", code: 200)
@idempotent
@documentation("Deletes a user from the system.")
operation DeleteUser {
    input: DeleteUserInput
    output: DeleteUserOutput
    errors: [
        NotFoundError
        UnauthorizedError
        InternalServerError
    ]
}

structure DeleteUserInput {
    @required
    @httpLabel
    userId: String
}

structure DeleteUserOutput {
    @required
    @httpPayload
    body: DeleteResponse
}

@http(method: "POST", uri: "/v1/users/search", code: 200)
@documentation("Searches for users by applying advanced filters.")
operation SearchUsers {
    input: SearchUsersInput
    output: SearchUsersOutput
    errors: [
        BadRequestError
        UnauthorizedError
        InternalServerError
    ]
}

structure SearchUsersInput {
    @required
    @httpPayload
    body: SearchRequest
}

structure SearchUsersOutput {
    @required
    @httpPayload
    body: ListUsersResponseBody
}

list UserResponseList {
    member: UserResponse
}

structure UserRequest {
    @required
    firstName: String

    @required
    lastName: String

    @required
    email: Email

    @required
    username: Username

    @required
    password: Password
}

structure UserResponse {
    @required
    userId: String

    @required
    email: String

    @required
    username: String

    @required
    firstName: String

    @required
    lastName: String

    @required
    createdByUser: String

    @required
    createdDate: String

    @required
    modifiedByUser: String

    @required
    modifiedDate: String
}

structure DeleteResponse {
    @required
    message: String

    @required
    success: Boolean
}
