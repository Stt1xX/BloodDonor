package com.bloodlink.exceptions;

public class CallerIsNotAUserException extends RuntimeException {
    public CallerIsNotAUserException(String message) {
        super(message);
    }
}
