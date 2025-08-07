package com.example.banto.Exceptions.CustomExceptions;

public class FailedPaymentException extends RuntimeException {
    public FailedPaymentException(String message){
        super(message);
    }
}
