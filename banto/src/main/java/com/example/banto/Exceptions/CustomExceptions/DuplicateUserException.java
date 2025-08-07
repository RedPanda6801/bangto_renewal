package com.example.banto.Exceptions.CustomExceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message){
        super(message);
    }
}
