package com.microservice.loans.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loans")
                        .description("Loans microservice REST API")
                        .version("V1")
                        .contact(new Contact()
                                .name("whitedevil")
                                .email("whitedevil@xyz.com")
                                .url("whitedevil.com"))
                        .license(new License()
                                .name("Apache")
                                .url("whitedevil.xyz")))
                .externalDocs(new ExternalDocumentation()
                        .url("whitedevil.com")
                        .description("REST APIs for Loans microservice"));
    }

}