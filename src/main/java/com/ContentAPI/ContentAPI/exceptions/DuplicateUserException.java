package com.ContentAPI.ContentAPI.exceptions;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message) {
        super(message);
    }
}
