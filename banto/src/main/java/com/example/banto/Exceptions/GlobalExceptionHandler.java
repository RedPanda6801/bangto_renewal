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

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResource(DuplicateResourceException e) {
        return ResponseEntity.badRequest().body(
            new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidLoginUser(InvalidCredentialsException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(TokenCreationException.class)
    public ResponseEntity<?> handleFailToCreateToken(TokenCreationException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage())
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(ResourceNotFoundException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(AlreadyProcessedException.class)
    public ResponseEntity<?> handleAlreadyProcessed(AlreadyProcessedException e) {
        return ResponseEntity.badRequest().body(
            new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgError(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(
            new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthentication(AuthenticationException e) {
        return ResponseEntity.badRequest().body(
            new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage())
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenException e) {
        return ResponseEntity.badRequest().body(
            new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage())
        );
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<?> handle500Error(InternalServerException e) {
        return ResponseEntity.internalServerError().body(
            new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage())
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessError(DataAccessException e) {
        return ResponseEntity.internalServerError().body(
            new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage())
        );
    }
}
