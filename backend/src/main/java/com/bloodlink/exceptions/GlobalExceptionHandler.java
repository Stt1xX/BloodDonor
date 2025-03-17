package com.bloodlink.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(CustomDuplicateException.class)
    public ResponseEntity<?> handleDuplicateExceptions(CustomDuplicateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalServiceArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleIllegalServiceArgumentException(IllegalServiceArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(CallerIsNotAUserException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public @ResponseBody String handleCallerIsNotAUser(CallerIsNotAUserException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public @ResponseBody String handleNotFound(ResourceNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public @ResponseBody String handleAuthException(AuthorizationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DtoConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleDTOConstraintViolation(DtoConstraintViolationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EntityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleEntityConstraintViolation(EntityConstraintViolationException e) {
        return e.getMessage();
    }

}
