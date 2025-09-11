package com.microservice.accounts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${auth.url}")
    private String authUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .opaqueToken(Customizer.withDefaults()))
//                                opaque -> opaque.introspector(opaqueTokenIntrospector())))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


//    @Bean
//    public OpaqueTokenIntrospector opaqueTokenIntrospector(){
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("accounts-service", "accounts-secret"));
//        return new SpringOpaqueTokenIntrospector(authUrl + "/oauth2/introspect", restTemplate);
//    }
}
