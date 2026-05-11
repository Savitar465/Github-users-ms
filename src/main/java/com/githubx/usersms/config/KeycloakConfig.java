package com.githubx.usersms.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak-client.server-url}")
    String serverUrl;

    @Value("${keycloak-client.client}")
    String client;

    @Value("${keycloak-client.client-secret}")
    String secretClient;

    @Value("${keycloak-client.realm}")
    String realm;

    @Bean
    Keycloak getRealmResource() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(client)
                .clientSecret(secretClient)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Bean
    public String keycloakBaseUrl() {
        return serverUrl + "/admin/realms/" + realm;
    }

}
