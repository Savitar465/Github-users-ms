$version: "2"

namespace com.github.users

structure UserDTO {
    @required
    id: String

    @required
    firstName: String

    @required
    lastName: String

    @required
    email: String

    @required
    username: String

    @required
    createdAt: String

    @required
    updatedAt: String
}
