package com.example.banto.Exceptions;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message){
        super(message);
    }
}
