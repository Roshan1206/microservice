package com.microservice.cards.controller;

import com.microservice.cards.dto.CardsDto;
import com.microservice.cards.dto.ErrorResponseDto;
import com.microservice.cards.dto.ResponseDto;
import com.microservice.cards.service.ICardsService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Cards", description = "REST APIs for Cards microservice")
@Validated
public class CardsController {

    @Autowired
    private ICardsService iCardsService;

    @ApiResponse(responseCode = "201", description = "CREATED")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits") @RequestParam String mobileNumber) {
        iCardsService.createCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED));
    }

    @ApiResponse(responseCode = "200", description = "Request processed successfully")
    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> getCardDetails(
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits") @RequestParam String mobileNumber) {
        CardsDto card = iCardsService.fetchCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(card);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "417", description = "EXPECTATION FAILED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCard(@Valid @RequestBody CardsDto card) {
        boolean isUpdated = iCardsService.updateCard(card);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "417", description = "EXPECTATION FAILED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCard(
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of 10 digits") @RequestParam String mobileNumber) {
        boolean isDeleted = iCardsService.deleteCard(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED));
    }

}
