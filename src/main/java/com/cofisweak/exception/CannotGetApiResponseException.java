package com.cofisweak.exception;

public class CannotGetApiResponseException extends RuntimeException {
    public CannotGetApiResponseException(String message) {
        super(message);
    }
}
