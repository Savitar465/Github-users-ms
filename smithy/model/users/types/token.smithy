$version: "2"

namespace com.github.users

structure AuthTokenDTO {
    @required
    accessToken: String

    @required
    expiresIn: Integer

    @required
    tokenType: String
}
