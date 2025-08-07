package com.example.banto.Exceptions.CustomExceptions;

public class TokenCreationException extends RuntimeException{
    public TokenCreationException(String message){
        super(message);
    }
}
