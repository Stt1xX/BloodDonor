package com.bloodlink.exceptions;

/**
 * Exception thrown when a resource conflict occurs (409 Conflict).
 * Examples: duplicate email, concurrent modification, etc.
 */
public class ConflictException extends BloodLinkException {
    
    public ConflictException(String message) {
        super(message);
    }
}
