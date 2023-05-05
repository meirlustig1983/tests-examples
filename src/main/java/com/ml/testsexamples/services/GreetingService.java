package com.ml.testsexamples.services;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String greeting() {
        return "Hello, World!";
    }

    public String greeting(String greet) {
        return String.format("Hello, %s!", greet);
    }
}