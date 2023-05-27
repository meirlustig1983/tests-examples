package com.ml.testsexamples.exceptions;

public class EmailValidationException extends RuntimeException {
    public EmailValidationException() {
        super("Wrong format exception");
    }
}