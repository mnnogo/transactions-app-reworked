package com.mnogo.transactions.exception;

public class BadCreditStateException extends RuntimeException {
    public BadCreditStateException(String message) {
        super(message);
    }
}
