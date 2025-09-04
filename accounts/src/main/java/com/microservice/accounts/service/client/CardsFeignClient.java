package com.microservice.accounts.service.client;

import com.microservice.accounts.config.FeignOAuth2Config;
import com.microservice.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

//in case loans microservice is down then it will fallback to its implementation class and perform operations
@FeignClient(name = "cards", fallback = CardsFallback.class, configuration = FeignOAuth2Config.class)
public interface CardsFeignClient {

    //    It should be same as the receiver method signature
    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("microservice-correlation-id") String correlationId,
                                                     @RequestParam String mobileNumber);

}
