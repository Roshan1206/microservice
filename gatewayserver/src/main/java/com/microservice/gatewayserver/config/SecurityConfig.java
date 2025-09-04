package com.microservice.gatewayserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${auth.url}")
    private String authUrl;


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/microservice/**").authenticated()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }


    /**
     * JWT Decoder for validating tokens from the authorization server
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder(){
        return ReactiveJwtDecoders.fromIssuerLocation(authUrl);
    }


    /**
     * JWT Authentication Converter to extract roles and scopes
     * Converts JWT claims to Spring Security authorities
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        // Set up authorities converter to handle both roles and scopes
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>();

            // Extract scopes (with SCOPE_ prefix)
            JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
            scopeConverter.setAuthoritiesClaimName("scopes");
            scopeConverter.setAuthorityPrefix("SCOPE_");
            Collection<GrantedAuthority> scopes = scopeConverter.convert(jwt);
            if (scopes != null) authorities.addAll(scopes);

            // Extract roles (with ROLE_ prefix or as-is from your token)
            JwtGrantedAuthoritiesConverter rolesConverter = new JwtGrantedAuthoritiesConverter();
            rolesConverter.setAuthoritiesClaimName("roles");
            rolesConverter.setAuthorityPrefix("");
            Collection<GrantedAuthority> roles = rolesConverter.convert(jwt);
            if (roles != null) authorities.addAll(roles);

            return authorities;
        });

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
