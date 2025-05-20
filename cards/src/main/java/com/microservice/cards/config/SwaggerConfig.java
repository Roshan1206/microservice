package com.microservice.cards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cards")
                        .description("Cards microservice")
                        .version("v1")
                        .contact(new Contact()
                                .name("whitedevil")
                                .email("whitedevil@xyz.com")
                                .url("whitedevil.com"))
                        .license(new License()
                                .name("Apache")
                                .url("whitedevil.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Cards microservice REST APIs"));
    }
}
