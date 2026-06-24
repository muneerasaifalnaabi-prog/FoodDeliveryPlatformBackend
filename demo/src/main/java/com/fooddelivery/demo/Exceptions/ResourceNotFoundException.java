package com.fooddelivery.demo.Exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    public ResourceNotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static ResourceNotFoundException notFound(String resourceName, Integer id) {
        return new ResourceNotFoundException(HttpStatus.NOT_FOUND, resourceName + " with ID " + id + " was not found.");
    }

    public HttpStatus getStatus() {
        return status;
    }
}