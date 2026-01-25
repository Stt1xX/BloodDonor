package com.bloodlink.exceptions;

/**
 * Exception thrown when user doesn't have permission to perform an action.
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
