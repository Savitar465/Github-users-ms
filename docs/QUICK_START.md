# Quick Start - Configuración local en 10 minutos

## Requisitos instalados
- Java 21+
- PostgreSQL 12+
- RabbitMQ
- Keycloak (corriendo, accesible)

## Pasos rápidos

### 1. Variables de entorno (Windows PowerShell)
```powershell
$env:SPRING_DATASOURCE_USUARIOS_URL = "jdbc:postgresql://localhost:5433/usuario_database"
$env:SPRING_DATASOURCE_USUARIOS_USERNAME = "jonaso"
$env:SPRING_DATASOURCE_USUARIOS_PASSWORD = "SnZz59*b8#Ff"

$env:SPRING_DATASOURCE_SGSIDB_URL = "jdbc:postgresql://localhost:5433/sgsidb"
$env:SPRING_DATASOURCE_SGSIDB_USERNAME = "fernando"
$env:SPRING_DATASOURCE_SGSIDB_PASSWORD = "W&WQt^bq3jTP"

$env:SPRING_RABBITMQ_HOST = "localhost"
$env:SPRING_RABBITMQ_USERNAME = "admin"
$env:SPRING_RABBITMQ_PASSWORD = "mC6Y*Vn2scvP"
```

### 2. Compilar y ejecutar
```bash
./mvnw clean package -q
./mvnw spring-boot:run
```

### 3. Verificar que esté levantado
```bash
# En otra terminal
curl -s http://localhost:8081/actuator/health | jq .
# Deberá responder: {"status":"UP"}
```

### 4. Acceder a documentación
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **API Docs**: http://localhost:8081/v3/api-docs

## Troubleshooting rápido
| Error | Solución |
|-------|----------|
| `Connection refused` a DB | Verificar PostgreSQL corriendo: `psql -h localhost -U jonaso` |
| `401 Unauthorized` en requests | Token faltante. Usar token válido en header `Authorization: Bearer <token>` |
| `Cannot connect to RabbitMQ` | Verificar RabbitMQ: `rabbitmqctl status` |

## Siguiente
Consultar [README.md](../README.md) para configuración completa.

