package com.microservice.cards.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Error Response", description = "Schema to hold error response")
public class ErrorResponseDto {

    private String apiPath;
    private HttpStatus httpStatus;
    private String errorMsg;
    private LocalDateTime errorTime;
}
