package com.bloodlink.exceptions;

/**
 * Exception thrown when user doesn't have permission to perform an action (403 Forbidden).
 */
public class AccessDeniedException extends BloodLinkException {
    
    public AccessDeniedException(String message) {
        super(message);
    }
}
