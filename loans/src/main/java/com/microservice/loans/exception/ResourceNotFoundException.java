package com.microservice.loans.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mobileNumber) {
        super("No loans found with the given mobile number : " + mobileNumber);
    }
}
