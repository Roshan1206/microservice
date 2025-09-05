package com.microservice.accounts.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@Configuration
public class FeignOAuth2Config {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager() {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }


    @Bean
    @Qualifier("jwtFeignInterceptor")
    public RequestInterceptor jwtFeignRequestInterceptor() {
        return template -> {
            // Get OAuth2 token for accounts-service client
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("accounts-service")
                    .principal("accounts-service")
                    .build();

            OAuth2AuthorizedClient authorizedClient = authorizedClientManager().authorize(authorizeRequest);
            if (authorizedClient != null && authorizedClient.getAccessToken() != null){
                // Add Authorization header to all Feign requests
                template.header("Authorization", "Bearer " + authorizedClient.getAccessToken().getTokenValue());
            }
        };
    }


    /**
     * Request Interceptor for services that require opaque tokens
     * You would configure a separate client registration for opaque tokens
     */
    @Bean
    @Qualifier("opaqueFeignInterceptor")
    public RequestInterceptor opaqueFeignRequestInterceptor(OAuth2AuthorizedClientManager clientManager) {
        return template -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("opaque-service-client")
                    .principal("opaque-service-client")
                    .build();

            OAuth2AuthorizedClient authorizedClient = clientManager.authorize(authorizeRequest);
            if (authorizedClient != null) {
                OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
                template.header("Authorization", "Bearer " + accessToken.getTokenValue());
            }
        };
    }
}
