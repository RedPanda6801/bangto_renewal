package com.example.banto.Exceptions.CustomExceptions;

public class AlreadyProcessedException extends RuntimeException {
    public AlreadyProcessedException(String message) {
        super(message);
    }
}