FROM eclipse-temurin:21-jdk-alpine
EXPOSE 8081
USER root

COPY target/Github-usuarios-ms.jar Github-usuarios-ms.jar
ENTRYPOINT ["java","-jar","/Github-usuarios-ms.jar"]