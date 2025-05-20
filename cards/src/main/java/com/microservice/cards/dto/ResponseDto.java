package com.microservice.cards.dto;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Response")
public class ResponseDto {
    // private String statusCode;
    // private String statusMessage;

    @Schema(description = "Response status code")
    private HttpStatus statusCode;
}
