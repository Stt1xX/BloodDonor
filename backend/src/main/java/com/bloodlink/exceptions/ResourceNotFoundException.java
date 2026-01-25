package com.bloodlink.exceptions;

/**
 * Exception thrown when a requested resource is not found (404 Not Found).
 */
public class ResourceNotFoundException extends BloodLinkException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

