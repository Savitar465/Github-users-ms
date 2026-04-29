$version: "2"

namespace com.github.users

use com.github.common#StringMap

@documentation("Tipos de búsqueda para filtrados avanzados")
structure SearchRequest {
    @required
    page: Integer

    @required
    size: Integer

    filters: StringMap
}
