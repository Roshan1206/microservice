package com.microservice.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "New Customer", description = "Schema for creating new customer")
public class NewCustomerDto {

    @NotEmpty
    @Schema(name = "Name", description = "Customer name", example = "John Doe")
    private String name;

    @NotEmpty
    @Email(message = "User must enter valid email")
    @Schema(name = "Email", description = "Customer email", example = "john@xyz.com")
    private String email;

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number should be 10 digits")
    @Schema(name = "Mobile number", description = "Customer mobile number", example = "9988776655")
    private String mobileNumber;
}
