package com.microservice.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Card", description = "Schema for holding Card object")
public class CardsDto {

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits")
    @Schema(name = "Mobile number", description = "User mobile number")
    private String mobileNumber;

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{16})", message = "Card number must be 16 digits")
    @Schema(name = "Card number", description = "User card number")
    private String cardNumber;

    @NotEmpty
    @Schema(name = "Card Type", description = "Visa/Mastercard")
    private String cardType;

    @NotEmpty
    @Schema(name = "Card limit", description = "Current card limit")
    private int totalAmount;

    @NotEmpty
    @Schema(name = "Available amount", description = "Available amount in card")
    private int availableAmount;

    @NotEmpty
    @Schema(name = "Amount used", description = "Remaining card limit")
    private int amountUsed;
}