package com.microservice.accounts.controller;

import com.microservice.accounts.constants.AccountsConstant;
import com.microservice.accounts.dto.AccountsContactInfoDto;
import com.microservice.accounts.dto.ErrorResponseDto;
import com.microservice.accounts.dto.ExistingCustomerDto;
import com.microservice.accounts.dto.NewCustomerDto;
import com.microservice.accounts.dto.ResponseDto;
import com.microservice.accounts.service.IAccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accounts controller", description = "REST APIs for Accounts controller")
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountsController {

    private final IAccountsService iAccountsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    public AccountsController(IAccountsService iAccountsService){
        this.iAccountsService = iAccountsService;
    }

    @Operation(summary = "Create new account", description = "REST API to create new Customer and Account")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @PostMapping("/create")
    public ResponseEntity<ExistingCustomerDto> createAccount(@Valid @RequestBody NewCustomerDto newCustomerDto){
        ExistingCustomerDto customer =  iAccountsService.createAccount(newCustomerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customer);
    }


    @Operation(summary = "Fetch account details", description = "REST API to fetch accounts details based on the mobile number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/fetch")
    public ResponseEntity<ExistingCustomerDto> fetchAccountDetails(
            @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits") String mobileNumber){
        ExistingCustomerDto customerDto = iAccountsService.fetchAccountDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerDto);
    }


    @Operation(summary = "Update account details", description = "REST API to update account details for customers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "417", description = "EXPECTATION FAILED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody ExistingCustomerDto customerDto){
        boolean isUpdated = iAccountsService.updateAccount(customerDto);

        if(isUpdated){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstant.STATUS_200, AccountsConstant.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(AccountsConstant.STATUS_417, AccountsConstant.MESSAGE_417_UPDATE));
    }


    @Operation(summary = "Delete account details", description = "REST API to delete account details for customers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "417", description = "EXPECTATION FAILED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(
            @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits") String mobileNumber){
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);

        if(isDeleted){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstant.STATUS_200, AccountsConstant.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(AccountsConstant.STATUS_417, AccountsConstant.MESSAGE_417_DELETE));
    }


    @Operation(summary = "Get build information", description = "Get build information that is deployed in accounts microservice")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }


//    fallback method for getBuildInfoFallback. Method signature and arguments should match with 1 extra argument Throwable
    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable){
        return ResponseEntity.status(HttpStatus.OK).body("0.9");
    }


    @Operation(summary = "Get java version", description = "Get java version that is deployed in accounts microservice")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
//    @RateLimiter(name = "getJavaVersion")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }


    @Operation(summary = "Get Contact info", description = "Get contact info that is deployed in accounts microservice")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfoDto);
    }
}
