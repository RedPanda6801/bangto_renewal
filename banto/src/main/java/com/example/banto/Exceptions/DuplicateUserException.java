package com.example.banto.Exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message){
        super(message);
    }
}
