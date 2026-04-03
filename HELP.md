# Ms-usuarios

## Descripción

Este es un proyecto Spring Boot llamado `ms-usuarios`, diseñado para funcionar como un microservicio para la gestión de usuarios.

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado el siguiente software:

- Java Development Kit (JDK) versión 19
- Servidor de base de datos PostgreSQL (dependencia de alcance de ejecución)
- Broker de mensajes RabbitMQ (dependencia de alcance de ejecución)

## Dependencias

El proyecto depende de varias bibliotecas y frameworks externos. Algunas de las dependencias clave incluyen:

- Spring Boot (versión 3.0.4)
- Spring Cloud (versión 2022.0.1)
- Hibernate JPA Model Generator (versión 6.2.0.Final)
- MapStruct (versión 1.5.2.Final) con Project Lombok
- Spring Cloud Netflix Eureka Client
- Spring Cloud Circuit Breaker con Reactor resilience4j
- Project Lombok para reducir el código boilerplate

Para obtener una lista completa de dependencias, consulta el archivo POM.

## Instalación

Para compilar y ejecutar el proyecto, sigue estos pasos:

1. Clona el repositorio.
2. Asegúrate de tener instalados los requisitos previos necesarios (ver arriba).
3. Ejecuta el siguiente comando para compilar el proyecto:
   ```bash
   ./mvnw clean package
   ```
4. Inicia el microservicio ejecutando:
    ```bash
    ./mvnw spring-boot:run
    ```
## Contribuciones

Agradecemos las contribuciones a este proyecto. Para contribuir, sigue estos pasos:

1. Haz un "Fork" (bifurcación) del repositorio.
2. Crea una nueva rama para tu funcionalidad o corrección de errores.
3. Realiza tus cambios y haz commits con mensajes descriptivos.
4. Sube tus cambios a tu repositorio "Fork".
5. Abre una "Pull Request" (solicitud de extracción) hacia la rama `dev` de este repositorio.

## Documentación

Para la documentación de la API y los endpoints, visita la siguiente URL después de ejecutar el proyecto:
http://localhost:8081/swagger-ui.html

