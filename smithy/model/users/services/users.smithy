$version: "2"

namespace com.github.users

use aws.protocols#restJson1

@title("Ms-Users - Users Management API")
@restJson1
@httpBearerAuth
@documentation("Microservice for user management with Keycloak integration for authentication and authorization.")
service UsersApi {
    version: "1.0.0"
    operations: [
        ListUsers
        GetUser
        CreateUser
        UpdateUser
        DeleteUser
        SearchUsers
    ]
}
