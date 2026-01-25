package com.bloodlink.exceptions;

/**
 * Exception thrown when a business rule is violated.
 * Examples: insufficient blood volume, request already processed, etc.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
