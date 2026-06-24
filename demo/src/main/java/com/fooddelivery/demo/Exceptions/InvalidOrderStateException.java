package com.fooddelivery.demo.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidOrderStateException extends RuntimeException {
    private final HttpStatus status;

    public InvalidOrderStateException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    public InvalidOrderStateException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public static InvalidOrderStateException conflict(String message) {
        return new InvalidOrderStateException(HttpStatus.CONFLICT, message);
    }
    public HttpStatus getStatus() {
        return status;
    }
}