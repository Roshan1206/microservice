package com.microservice.loans.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private String apiPath;
    private HttpStatus status;
    private String error;
    private LocalDateTime time;
}
