package com.bloodlink.exceptions;

public class DtoConstraintViolationException extends RuntimeException {

    public DtoConstraintViolationException(String message) {
        super(message);
    }
}
