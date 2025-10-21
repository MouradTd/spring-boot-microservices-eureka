package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .pathMatchers("/api/ressource/**").hasRole("ressource-manager")
                    .pathMatchers("/api/patient/**").hasRole("patient-manager")
                    .pathMatchers("/actuator/prometheus").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
            ));

        return http.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            grantedAuthoritiesConverter.setAuthorityPrefix(""); // Remove default "ROLE_" prefix

            // Extract roles from "resource_access.centreDentaire.roles"
            JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
            authenticationConverter.setJwtGrantedAuthoritiesConverter(jwtToken -> {
                var authorities = grantedAuthoritiesConverter.convert(jwtToken);
                var resourceAccess = (Map<String, Object>) jwtToken.getClaimAsMap("resource_access");
                var centreDentaire = (Map<String, Object>) resourceAccess.getOrDefault("centreDentaire", Map.of());
                var roles = (List<String>) centreDentaire.getOrDefault("roles", List.of());
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                return authorities;
            });

            return Mono.justOrEmpty(authenticationConverter.convert(jwt));
        };
    }
}