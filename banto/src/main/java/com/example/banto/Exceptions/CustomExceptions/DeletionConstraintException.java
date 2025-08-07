package com.example.banto.Exceptions.CustomExceptions;

public class DeletionConstraintException extends RuntimeException {
    public DeletionConstraintException(String message){
        super(message);
    }
}
