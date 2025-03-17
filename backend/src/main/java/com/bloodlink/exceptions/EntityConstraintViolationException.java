package com.bloodlink.exceptions;

public class EntityConstraintViolationException extends RuntimeException {

    public EntityConstraintViolationException(String message) {
        super(message);
    }
}
