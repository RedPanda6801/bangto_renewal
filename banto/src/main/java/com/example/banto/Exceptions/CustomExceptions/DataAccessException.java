package com.example.banto.Exceptions.CustomExceptions;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message){
        super(message);
    }
}
