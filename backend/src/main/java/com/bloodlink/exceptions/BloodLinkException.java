package com.bloodlink.exceptions;

/**
 * Base exception for all BloodLink application exceptions.
 * All custom exceptions should extend this class.
 */
public abstract class BloodLinkException extends RuntimeException {
    
    public BloodLinkException(String message) {
        super(message);
    }
    
    public BloodLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
