package com.example.banto.Exceptions.CustomExceptions;

public class InternalServerException extends RuntimeException{
    public InternalServerException(String message){
        super(message);
    }
}
