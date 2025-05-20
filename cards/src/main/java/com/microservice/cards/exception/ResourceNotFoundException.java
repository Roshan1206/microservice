package com.microservice.cards.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mobileNumber) {
        super(String.format("No card found with the given mobile number : %s", mobileNumber));
    }
}
