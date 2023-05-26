package com.ml.testsexamples.exceptions;

public class InactiveAccountException extends RuntimeException {
    public InactiveAccountException() {
        super("Inactive bank account");
    }
}