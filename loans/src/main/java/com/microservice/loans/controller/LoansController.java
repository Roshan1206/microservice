package com.microservice.loans.controller;

import com.microservice.loans.dto.ErrorResponseDto;
import com.microservice.loans.dto.LoansCotactInfoDto;
import com.microservice.loans.dto.LoansDto;
import com.microservice.loans.service.ILoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Loans", description = "Loans REST APIs")
public class LoansController {

    @Autowired
    private ILoanService iLoanService;

    @Autowired
    private Environment environment;

    @Autowired
    private LoansCotactInfoDto loansCotactInfoDto;

    @Value("${build.version}")
    private String buildVersion;

    @ApiResponse(responseCode = "201", description = "Loans creates successfully")
    @PostMapping("/create")
    public ResponseEntity<LoansDto> createLoan(
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits only") @RequestParam String mobileNumber) {
        LoansDto loan = iLoanService.createLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @ApiResponse(responseCode = "200", description = "Loan retrieved successfully")
    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoan(
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits only") @RequestParam String mobileNumber) {
        LoansDto loan = iLoanService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loan);
    }

    @ApiResponse(responseCode = "200", description = "Loan updated successfully")
    @PutMapping("/update")
    public ResponseEntity<LoansDto> updateLoan(@Valid @RequestBody LoansDto loansDto) {
        LoansDto updateLoan = iLoanService.updateLoan(loansDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateLoan);
    }

    @ApiResponse(responseCode = "200", description = "Loan deleted successfully")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteLoan(
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits only") @RequestParam String mobileNumber) {
        iLoanService.deleteLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body("Loan deleted successfully");
    }


    @Operation(summary = "Get build information", description = "Get build information that is deployed in loans microservice")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }


    @Operation(summary = "Get java version", description = "Get java version that is deployed in loans microservice")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }


    @Operation(summary = "Get Contact info", description = "Get contact info that is deployed in loans microservice")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<LoansCotactInfoDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(loansCotactInfoDto);
    }

}