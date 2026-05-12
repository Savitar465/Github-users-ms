FROM eclipse-temurin:25-jdk-alpine
LABEL org.opencontainers.image.title="Github-users-ms" \
	org.opencontainers.image.description="Microservicio de usuarios (autenticacion/perfil)" \
	org.opencontainers.image.vendor="Githubx" \
	org.opencontainers.image.url="https://github.com/Savitar465/Github-users-ms" \
	org.opencontainers.image.source="https://github.com/Savitar465/Github-users-ms" \
	org.opencontainers.image.documentation="https://github.com/Savitar465/Github-users-ms/blob/main/README.md" \
	org.opencontainers.image.authors="Savitar465"
EXPOSE 8081
USER root

COPY target/Github-usuarios-ms.jar Github-usuarios-ms.jar
ENTRYPOINT ["java","-jar","/Github-usuarios-ms.jar"]