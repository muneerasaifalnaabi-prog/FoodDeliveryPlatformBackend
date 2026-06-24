package com.fooddelivery.demo.Exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends RuntimeException {
    private final HttpStatus status;

    public DuplicateResourceException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    public DuplicateResourceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static DuplicateResourceException alreadyExists(String resourceName, String value) {

        return new DuplicateResourceException(HttpStatus.CONFLICT, resourceName + " '" + value + "' already exists.");
    }

    public HttpStatus getStatus() {
        return status;
    }
}