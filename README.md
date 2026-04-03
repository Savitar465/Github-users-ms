# ms-usuarios

Microservicio para la gestión centralizada de usuarios, autenticación OAuth2/JWT y autorización con Keycloak.
Construido con Spring Boot 3.x, persistencia multi-datasource (usuarios + sgsidb) y mensajería asincrónica.

## 📚 Documentación

| Documento | Descripción | Tiempo |
|-----------|-------------|--------|
| **[QUICK_START.md](./QUICK_START.md)** | Setup local + variables entorno | 10 min |
| **[README-STARTER.md](./README-STARTER.md)** | Onboarding devs (convenciones + Do/Don't) | 15 min |
| **[ARCHITECTURE.md](./ARCHITECTURE.md)** | Diseño, patrones, CRUD canónico (genérico, reutilizable) | 30 min |
| **[CONTRIBUTING.md](./CONTRIBUTING.md)** | Cómo contribuir (branches, PR, checklist) | 20 min |

## 📋 Tabla de contenidos
- [Descripción](#descripción)
- [Características](#características)
- [Requisitos previos](#requisitos-previos)
- [Instalación y configuración](#instalación-y-configuración)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Guías de desarrollo](#guías-de-desarrollo)
- [API](#api)
- [Seguridad](#seguridad)
- [Despliegue](#despliegue)
- [Solución de problemas](#solución-de-problemas)
- [Contribuciones](#contribuciones)

## Descripción

`ms-usuarios` es un microservicio Spring Boot que centraliza la gestión de usuarios en una arquitectura de microservicios.
Integra Keycloak para autenticación/autorización, persiste datos en múltiples bases de datos PostgreSQL,
y publica eventos de usuario a través de RabbitMQ.

**Versión actual**: 2.5.0
**Java**: 21+
**Spring Boot**: 3.2.0

## Características

- **Gestión de usuarios**: CRUD completo, búsqueda, paginación
- **Autenticación OAuth2**: JWT con Keycloak Policy Enforcer
- **Multi-datasource**: contexto `usuarios` + contexto `sgsidb`
- **Mensajería asincrónica**: eventos de usuario a RabbitMQ
- **Documentación OpenAPI**: Swagger UI en `/swagger-ui.html`
- **Service discovery**: integración con Eureka
- **Resilencia**: circuit breaker (resilience4j)
- **Logging**: structured con Logbook + SLF4J
- **Containerización**: Docker + manifiestos Kubernetes (kompose)

## Requisitos previos

### Software
- **Java Development Kit (JDK)** 21+
- **Maven** 3.8+ (o usar `mvnw`)
- **PostgreSQL** 12+ (dos instancias o schemas separados)
- **RabbitMQ** 3.x+ (message broker)
- **Keycloak** (servidor de identidad, versión 26.0.1+)
- **Docker** (opcional, para containerización)

### Credenciales y acceso
- Acceso a PostgreSQL (usuarios: `jonaso`, `fernando`)
- Cliente Keycloak: `admin-java` con credenciales configuradas
- URL base Keycloak: `https://keycloakqa.apps.gob.bo`
- Realm: `test`

## Instalación y configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/<org>/ms-usuarios.git
cd ms-usuarios
```

### 2. Configurar variables de entorno (desarrollo local)
Crea un archivo `.env` en la raíz (o configura en IDE):

```bash
# Datasources
DB_USUARIOS_URL=jdbc:postgresql://localhost:5433/usuario_database?currentSchema=usuarios_nuevo
DB_USUARIOS_USER=jonaso
DB_USUARIOS_PASS=SnZz59*b8#Ff

DB_SGSIDB_URL=jdbc:postgresql://localhost:5433/sgsidb?currentSchema=pruebas_sgsi
DB_SGSIDB_USER=fernando
DB_SGSIDB_PASS=W&WQt^bq3jTP

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=admin
RABBITMQ_PASS=mC6Y*Vn2scvP

# Keycloak
KEYCLOAK_URL=https://keycloakqa.apps.gob.bo
KEYCLOAK_REALM=test
KEYCLOAK_CLIENT=admin-java
KEYCLOAK_SECRET=O6Jxhw2VWPaXozGVG97Hwasbzyaq91UX
```

### 3. Compilar el proyecto
```bash
./mvnw clean package
```

### 4. Ejecutar en desarrollo
```bash
# Con perfil por defecto
./mvnw spring-boot:run

# Con perfil específico (staging, prod)
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=staging
```

El servicio estará disponible en `http://localhost:8081`.

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/inspire/msusuarios/
│   │   ├── config/              # Configuraciones globales
│   │   │   ├── security/        # JWT + Keycloak Policy Enforcer
│   │   │   ├── amqp/            # RabbitMQ
│   │   │   └── ConfigurationOpenApi.java
│   │   │
│   │   ├── controller/          # Endpoints REST (/v1/*)
│   │   │   ├── UsuarioController.java
│   │   │   ├── AreaController.java
│   │   │   └── ...
│   │   │
│   │   ├── service/             # Lógica de negocio
│   │   │   ├── contratos/       # Interfaces
│   │   │   └── implementacion/  # Implementaciones
│   │   │
│   │   ├── dao/                 # Repositorios JPA
│   │   │   ├── usuarios/
│   │   │   └── sgsidb/
│   │   │
│   │   ├── model/               # Entidades JPA
│   │   │   ├── usuarios/
│   │   │   └── sgsidb/
│   │   │
│   │   ├── mapper/              # MapStruct converters
│   │   ├── dto/                 # DTOs request/response
│   │   ├── util/                # Utilidades
│   │   ├── datasources/         # Configuración datasources
│   │   └── MsUsuariosApplication.java
│   │
│   └── resources/
│       ├── application.yaml
│       ├── application-staging.yaml
│       ├── application-prod.yaml
│       └── policy-enforcer-*.json  # Políticas Keycloak
│
└── test/
    ├── java/...
    └── resources/
```

Más detalles en [ARCHITECTURE.md](./ARCHITECTURE.md).

## Guías de desarrollo

### Para desarrolladores nuevos
Lee primero: [README-STARTER.md](./README-STARTER.md)

Incluye:
- Conceptos clave de arquitectura
- Flujo de trabajo recomendado
- Do/Don't para PR reviews
- Prompt para IA

### Para implementar un nuevo módulo
1. Seguir la plantilla en [ARCHITECTURE.md](./ARCHITECTURE.md#8-plantilla-minima-para-nuevos-modulos-de-dominio)
2. Respetar nomenclatura: `*Controller`, `*Service`, `*Dao`, `*Mapper`
3. Endpoints bajo `/v1/<recurso>`
4. Usar DTOs en request/response (sin exponer entidades JPA)
5. Documentar con OpenAPI

### Ejemplo CRUD completo
Consulta la sección "Ejemplo canonico CRUD completo" en [ARCHITECTURE.md](./ARCHITECTURE.md#10-ejemplo-canonico-crud-completo).

## API

### Documentación interactiva
- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`

### Endpoints principales

#### Usuarios
```
GET    /v1/usuarios                    # Listar (paginado)
GET    /v1/usuarios/{usuarioKyId}     # Obtener por ID
POST   /v1/usuarios                    # Crear
PUT    /v1/usuarios/{usuarioKyId}     # Actualizar
DELETE /v1/usuarios/{usuarioKyId}     # Eliminar (soft delete)
POST   /v1/usuarios/search             # Buscar por filtro
```

#### Áreas
```
GET    /v1/areas
POST   /v1/areas
PUT    /v1/areas/{id}
DELETE /v1/areas/{id}
```

#### Equipos, Roles, Cargos, Sistemas, Clientes
Endpoints similares bajo `/v1/<recurso>`.

### Ejemplo de petición
```bash
curl -X GET "http://localhost:8081/v1/usuarios?pagina=0&cantidad=10" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json"
```

## Seguridad

### Autenticación
- OAuth2 Resource Server con JWT
- Issuer: `https://keycloakqa.apps.gob.bo/realms/test`
- Validación de firma en `jwk-set-uri`

### Autorización
- Roles extraídos desde token JWT (`realm_access.roles`)
- Políticas de acceso en archivos `policy-enforcer-*.json`
- Anotaciones `@PreAuthorize`, `@PostAuthorize` en controllers/services

### Best practices
- ✅ No versionar secretos ni credenciales
- ✅ Usar variables de entorno o Secret Manager
- ✅ Rotación periódica de cliente secret
- ✅ Logs sin datos sensibles
- ✅ HTTPS obligatorio en producción

## Despliegue

### Docker
```bash
# Compilar imagen
docker build -t ms-usuarios:2.5.0 .

# Ejecutar contenedor
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_USUARIOS_URL=jdbc:postgresql://host:5433/usuario_database \
  -e SPRING_DATASOURCE_USUARIOS_USERNAME=jonaso \
  -e SPRING_DATASOURCE_USUARIOS_PASSWORD=<pwd> \
  ms-usuarios:2.5.0
```

### Kubernetes
```bash
# Aplicar manifiestos
kubectl apply -f kubeconf/ms-usuarios-deployment.yaml
kubectl apply -f kubeconf/ms-usuarios-service.yaml

# Verificar despliegue
kubectl get pods -l app=ms-usuarios
kubectl logs -f deployment/ms-usuarios
```

Ver manifiestos en `kubeconf/`.

### Ambientes
- **dev**: rama `develop`, perfil default
- **staging**: rama `staging`, perfil `staging`, config desde Config Server
- **prod**: rama `main`, perfil `prod`, secretos desde Secret Manager

## Solución de problemas

### "Cannot connect to database"
- Verificar variables de entorno: `DB_USUARIOS_URL`, `DB_SGSIDB_URL`
- Probar conectividad: `psql -h <host> -U <user> -d <database>`

### "401 Unauthorized"
- Validar token JWT: `https://jwt.io` (paste token)
- Verificar `issuer-uri` y `jwk-set-uri` en `application.yaml`
- Asegurar que el token contiene claim `user_db_id`

### "RabbitMQ connection refused"
- Verificar que RabbitMQ está corriendo: `rabbitmq-plugins list`
- Validar variables: `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USER`, `RABBITMQ_PASS`

### Logs y debugging
```bash
# Ver logs en tiempo real
./mvnw spring-boot:run -Dlogging.level.root=DEBUG

# Log específico por paquete
-Dlogging.level.com.inspire.msusuarios=DEBUG
```

## Contribuciones

### Flujo de contribución
1. Fork el repositorio
2. Crear rama: `git checkout -b feat/nueva-funcionalidad`
3. Hacer cambios respetando [ARCHITECTURE.md](docs/ARCHITECTURE.md)
4. Commits descriptivos: `git commit -m "feat: agregar X funcionalidad"`
5. Push: `git push origin feat/nueva-funcionalidad`
6. Abrir PR contra rama `dev` o `main`

### Checklist de PR
Usa template automático (`.github/pull_request_template.md`):
- [ ] Respetar arquitectura por capas
- [ ] Endpoints versionados `/v1`
- [ ] DTOs usados (sin exponer entidades JPA)
- [ ] Validaciones completas
- [ ] Tests agregados
- [ ] Sin secretos en código
- [ ] OpenAPI documentado

### Tipos de issues
- [**Bug**](.github/ISSUE_TEMPLATE/bug_report.md): defectos
- [**Feature**](.github/ISSUE_TEMPLATE/feature_request.md): nuevas funcionalidades

## Contacto y soporte

- **Equipo**: Inspire (Development team)
- **Issues**: Usar GitHub Issues con templates
- **Documentación interna**: [Confluence/Wiki]
- **Canales Slack**: `#ms-usuarios-dev`, `#microservicios`

---

**Última actualización**: 2026-04-02
Para más detalles, consulta [ARCHITECTURE.md](docs/ARCHITECTURE.md) y [README-STARTER.md](docs/README-STARTER.md).


