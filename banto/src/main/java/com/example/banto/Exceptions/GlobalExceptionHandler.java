package com.example.banto.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<?> handleDuplicateUser(DuplicateUserException e) {
        return ResponseEntity.badRequest().body(
            new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }
}
