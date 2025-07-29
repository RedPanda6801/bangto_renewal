package com.example.banto.Exceptions;

public class DeletionConstraintException extends RuntimeException {
    public DeletionConstraintException(String message){
        super(message);
    }
}
