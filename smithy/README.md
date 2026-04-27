# GitHub — Proyecto Smithy

Modelo de API REST en Smithy 2.0 para el dominio de archivos del proyecto GitHub.

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
│   └── files/
│       ├── services/
│       │   └── files-api.smithy
│       ├── operations/
│       │   ├── content-operations.smithy
│       │   ├── write-operations.smithy
│       │   └── commits-operations.smithy
│       └── structures/
│           ├── content-structures.smithy
│           ├── write-structures.smithy
│           └── commits-structures.smithy
└── README.md
```

## Requisitos

- Java 17 o superior
- Conexión a internet para descargar dependencias la primera vez

## Primeros pasos

### 1) Validar el modelo Smithy

```powershell
Set-Location "C:\Projects\Github-files-ms\smithy"
.\gradlew.bat smithyBuild --no-daemon
```

Esto valida el modelo y genera las proyecciones configuradas en `smithy-build.json`.

### 2) Generar OpenAPI

```powershell
Set-Location "C:\Projects\Github-files-ms\smithy"
.\gradlew.bat smithyBuild --no-daemon
```

La salida OpenAPI queda en `build/smithyprojections/.../openapi/`.

### 3) Generar código Spring / TypeScript desde OpenAPI

```powershell
Set-Location "C:\Projects\Github-files-ms\smithy"
.\gradlew.bat generateAllCodegen --no-daemon
```

O, si quieres por separado:

```powershell
Set-Location "C:\Projects\Github-files-ms\smithy"
.\gradlew.bat generateAllTypeScriptClients --no-daemon
.\gradlew.bat generateAllJavaServers --no-daemon
```

## Salidas generadas

- **OpenAPI**: `build/smithyprojections/<project>/<projection>/openapi/*.openapi.json`
- **Cliente TypeScript**: `build/generated/typescript/files-client`
- **Server Java Spring**: `build/generated/spring/files-module`

Cada módulo Spring es completamente independiente e incluye:
- Controllers y delegados (delegate pattern)
- Modelos con enums propios
- `EnumConverterConfiguration.java` para conversión automática de enumeraciones
- `pom.xml` con todas las dependencias necesarias
- Aplicación Spring Boot autoejecutable

Estructura de paquetes en cada módulo:
```
com.smithy.g.files.server.files.api      (controllers)
com.smithy.g.files.server.files.model    (DTOs y enums)
com.smithy.g.files.server.files.invoker  (app principal)
org.openapitools.configuration             (enum converters)
```

## Generación del dominio Files

El proyecto genera un único módulo Spring para `FilesApi`.

Generar directamente el módulo Files:

```powershell
.\gradlew.bat generateFilesJavaServer --no-daemon
.\gradlew.bat generateFilesTypeScriptClient --no-daemon
```

## Convenciones del modelo

- `FilesApi` expone dos variantes para contenido: por `path` query y por ruta explícita `{filePath+}`.
- Algunos generadores OpenAPI emiten advertencias con labels greedies (`{filePath+}`), pero el modelo valida correctamente.

## Autenticación

Las operaciones protegidas usan `@httpBearerAuth`.

Header requerido:

```text
Authorization: Bearer <jwt_token>
```

El token se obtiene en `POST /v1/auth/login`.
