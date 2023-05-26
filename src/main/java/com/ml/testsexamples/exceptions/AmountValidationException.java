package com.ml.testsexamples.exceptions;

public class AmountValidationException extends RuntimeException {
    public AmountValidationException() {
        super("Amount number should be positive");
    }
}