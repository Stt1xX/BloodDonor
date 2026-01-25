package com.bloodlink.exceptions;

/**
 * Exception thrown when validation fails (400 Bad Request).
 * Use for input validation errors.
 */
public class ValidationException extends BloodLinkException {
    
    public ValidationException(String message) {
        super(message);
    }
}
