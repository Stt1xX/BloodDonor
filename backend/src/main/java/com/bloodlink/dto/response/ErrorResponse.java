package com.bloodlink.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Unified error response DTO for all API errors.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String code,
    String message,
    LocalDateTime timestamp,
    List<FieldError> fieldErrors
) {
    public ErrorResponse(String code, String message) {
        this(code, message, LocalDateTime.now(), null);
    }
    
    public ErrorResponse(String code, String message, List<FieldError> fieldErrors) {
        this(code, message, LocalDateTime.now(), fieldErrors);
    }
    
    /**
     * Represents a single field validation error.
     */
    public record FieldError(
        String field,
        String message
    ) {}
}
