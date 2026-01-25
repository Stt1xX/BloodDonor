package com.bloodlink.exceptions;

/**
 * Exception thrown when a business rule is violated (422 Unprocessable Entity).
 * Examples: insufficient blood volume, request already processed, etc.
 */
public class BusinessException extends BloodLinkException {
    
    public BusinessException(String message) {
        super(message);
    }
}
