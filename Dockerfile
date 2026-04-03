FROM eclipse-temurin:21-jdk-alpine
EXPOSE 8081
USER root
COPY certs/keycloakqa.apps.gob.bo.crt $JAVA_HOME/lib/security
RUN \
    cd $JAVA_HOME/lib/security \
    && keytool -keystore cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias keycloakcert -file keycloakqa.apps.gob.bo.crt
COPY target/ms-usuarios.jar ms-usuarios.jar
ENTRYPOINT ["java","-jar","/ms-usuarios.jar"]