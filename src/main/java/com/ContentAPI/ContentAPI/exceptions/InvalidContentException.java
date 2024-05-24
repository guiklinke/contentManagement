package com.ContentAPI.ContentAPI.exceptions;

public class InvalidContentException extends RuntimeException {
    public InvalidContentException() {
        super();
    }

    public InvalidContentException(String message) {
        super(message);
    }

    public InvalidContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
