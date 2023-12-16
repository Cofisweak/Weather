package com.cofisweak.exception;

public class LocationAlreadyExistsException extends RuntimeException {
    public LocationAlreadyExistsException(String message) {
        super(message);
    }
}
