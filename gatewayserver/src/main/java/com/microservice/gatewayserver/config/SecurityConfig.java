package com.microservice.gatewayserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringReactiveOpaqueTokenIntrospector;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

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
                        .opaqueToken(opaque -> opaque.introspector(opaqueTokenIntrospector())))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }


    @Bean
    public ReactiveOpaqueTokenIntrospector opaqueTokenIntrospector() {
        WebClient client = WebClient.builder()
                .defaultHeaders(headers -> headers
                .setBasicAuth("secure-service", "secure-secret"))
                .build();
        return new SpringReactiveOpaqueTokenIntrospector(authUrl + "/oauth2/introspect", client);
    }
}
