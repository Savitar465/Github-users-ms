# Ms-Usuarios — Proyecto Smithy

Modelo de API REST en Smithy 2.0 para el dominio de usuarios del microservicio `Ms-Usuarios`, con separación explícita entre `usuarios`, `keycloak` y `common`.

## Visión general

Este módulo mantiene el contrato de la API en Smithy y genera artefactos derivados para OpenAPI y Spring Boot.

Flujo recomendado:

```text
Smithy model -> smithyBuild -> OpenAPI -> openapi-generator (Spring / TypeScript)
```

## Estructura

```text
smithy/
├── build.gradle
├── settings.gradle
├── smithy-build.json
├── model/
│   ├── common/
│   │   └── common.smithy
│   └── users/
│       ├── services/
│       │   ├── usuarios.smithy
│       │   └── keycloak.smithy
│       ├── operations/
│       │   ├── usuarios.smithy
│       │   ├── keycloak-clients.smithy
│       │   ├── keycloak-roles.smithy
│       │   └── keycloak-permissions.smithy
│       └── types/
│           ├── oauth.smithy
│           └── token.smithy
└── README.md
```

## Requisitos

- Java 17 o superior
- Conexión a internet para descargar dependencias la primera vez

## Primeros pasos

### 1) Validar el modelo Smithy

```powershell
Set-Location "C:\Projects\Ms-Usuarios\smithy"
.\gradlew.bat smithyBuild --no-daemon
```

Esto valida el modelo y genera las proyecciones configuradas en `smithy-build.json`.

### 2) Generar OpenAPI

```powershell
Set-Location "C:\Projects\Ms-Usuarios\smithy"
.\gradlew.bat smithyBuild --no-daemon
```

La salida OpenAPI queda en `build/smithyprojections/.../openapi/`.

### 3) Generar código Spring / TypeScript desde OpenAPI

```powershell
Set-Location "C:\Projects\Ms-Usuarios\smithy"
.\gradlew.bat generateAllCodegen --no-daemon
```

O, si quieres por separado:

```powershell
Set-Location "C:\Projects\Ms-Usuarios\smithy"
.\gradlew.bat generateAllTypeScriptClients --no-daemon
.\gradlew.bat generateAllJavaServers --no-daemon
```

## Salidas generadas

- **OpenAPI**: `build/smithyprojections/<project>/<projection>/openapi/*.openapi.json`
- **Cliente TypeScript**: `build/generated/typescript/usuarios-client` y `build/generated/typescript/keycloak-client`
- **Server Java Spring**: `build/generated/spring/usuarios-module` y `build/generated/spring/keycloak-module`

Cada módulo Spring es completamente independiente e incluye:
- Controllers y delegados (delegate pattern)
- Modelos con enums propios
- `EnumConverterConfiguration.java` para conversión automática de enumeraciones
- `pom.xml` con todas las dependencias necesarias
- Aplicación Spring Boot autoejecutable

Estructura de paquetes en cada módulo generado:
```
com.github.users.usuarios.server.usuarios.api      (controllers)
com.github.users.usuarios.server.usuarios.model    (DTOs)
com.github.users.usuarios.server.usuarios.invoker  (app principal)

com.github.users.keycloak.server.keycloak.api      (controllers)
com.github.users.keycloak.server.keycloak.model    (DTOs)
com.github.users.keycloak.server.keycloak.invoker  (app principal)

org.openapitools.configuration                     (configuración común)
```

## Generación por dominio

El proyecto genera dos módulos Spring separados:

- `UsuariosApi` para el dominio de usuarios
- `KeycloakManagementApi` para el dominio de Keycloak

Generar cada uno de forma independiente:

```powershell
Set-Location "C:\Projects\Ms-Usuarios\smithy"
.\gradlew.bat generateUsuariosJavaServer --no-daemon
.\gradlew.bat generateKeycloakJavaServer --no-daemon
```

Generar clientes TypeScript:

```powershell
Set-Location "C:\Projects\Ms-Usuarios\smithy"
.\gradlew.bat generateUsuariosTypeScriptClient --no-daemon
.\gradlew.bat generateKeycloakTypeScriptClient --no-daemon
```

## Convenciones del modelo

- `UsuariosApi` cubre el CRUD de usuarios y la búsqueda avanzada.
- `KeycloakManagementApi` cubre clientes, roles y permisos de Keycloak.
- Los shapes compartidos viven en `com.github.users.common`.

## Autenticación

Las operaciones protegidas usan `@httpBearerAuth`.

Header requerido:

```text
Authorization: Bearer <jwt_token>
```

