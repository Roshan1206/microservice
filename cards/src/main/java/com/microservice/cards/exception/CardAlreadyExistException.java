package com.microservice.cards.exception;

public class CardAlreadyExistException extends RuntimeException {

    public CardAlreadyExistException(String message) {
        super(message);
    }
}
