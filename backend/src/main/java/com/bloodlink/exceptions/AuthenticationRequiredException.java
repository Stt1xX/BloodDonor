package com.bloodlink.exceptions;

/**
 * Exception thrown when user is not authenticated (401 Unauthorized).
 */
public class AuthenticationRequiredException extends BloodLinkException {
    
    public AuthenticationRequiredException(String message) {
        super(message);
    }
}
