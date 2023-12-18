package com.cofisweak.exception;

public class LocationAlreadyExistsException extends RuntimeException {
    public LocationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
