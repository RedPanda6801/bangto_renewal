package com.example.banto.Exceptions;

public class FailedPaymentException extends RuntimeException {
    public FailedPaymentException(String message){
        super(message);
    }
}
