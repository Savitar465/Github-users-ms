package com.githubx.usuariosms.config.security;

import jakarta.annotation.PostConstruct;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private PolicyEnforcerConfig combinedPolicy;

    @PostConstruct
    public void initPolicies() {
        try {
            PolicyEnforcerConfig policy = JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer-allow.json"),
                    PolicyEnforcerConfig.class);
            PolicyEnforcerConfig policyUsers = JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer-usuarios.json"),
                    PolicyEnforcerConfig.class);
            PolicyEnforcerConfig policyRoles = JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer-roles.json"),
                    PolicyEnforcerConfig.class);
            policy.getPaths().addAll(policyRoles.getPaths());
            policy.getPaths().addAll(policyUsers.getPaths());
            this.combinedPolicy = policy;
        } catch (IOException e) {
            throw new RuntimeException("Error loading policy files", e);
        }
    }

    @Bean
    public PolicyEnforcerConfig combinedPolicyConfig() {
        return this.combinedPolicy;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http.addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class);
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }

    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
        return new ServletPolicyEnforcerFilter(httpRequest -> combinedPolicyConfig());
    }
}