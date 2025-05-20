package com.microservice.loans.exception;

public class LoanAlreadyExistsException extends RuntimeException {
    public LoanAlreadyExistsException(String mobileNumber) {
        super("Loan already exist with the given mobile number : " + mobileNumber);
    }
}
