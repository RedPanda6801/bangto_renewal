package com.example.banto.Exceptions;

public class InternalServerException extends RuntimeException{
    public InternalServerException(String message){
        super(message);
    }
}
