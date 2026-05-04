$version: "2"

namespace com.github.users

use com.github.common#StringMap

@documentation("Search types for advanced filtering")
structure SearchRequest {
    @required
    page: Integer

    @required
    size: Integer

    filters: StringMap
}
