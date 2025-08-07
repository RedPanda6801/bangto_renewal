package com.example.banto.Exceptions.CustomExceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message){
        super(message);
    }
}
