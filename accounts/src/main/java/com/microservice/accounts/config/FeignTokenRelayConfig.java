package com.microservice.accounts.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
@Profile("token_relay")
public class FeignTokenRelayConfig {


     /**
     * Feign interceptor that forwards the current user's JWT to downstream
     * services by adding an Authorization header.
     *
     * <p>This enables <b>Token Relay</b>: the same token a user sends to
     * Accounts is propagated to Cards/Loans so each service can validate
     * the original user identity.</p>
     *
     * @return a {@link RequestInterceptor} that adds the JWT as a Bearer token
     */
    @Bean
    public RequestInterceptor tokenRelayRequestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken){
                String jwtToken = jwtAuthenticationToken.getToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + jwtToken);
            }
        };
    }
}
