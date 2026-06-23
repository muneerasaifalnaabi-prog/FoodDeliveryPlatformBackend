package com.fooddelivery.demo.Exceptions;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {
    private final HttpStatus status;

    public GenericException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GenericException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
