package com.ml.testsexamples.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreetingServiceTest {

    private GreetingService service;

    @BeforeEach
    public void init() {
        service = new GreetingService();
    }

    @Test
    public void greetingTest() {
        assertEquals("Hello, World!", service.greeting());
    }

    @Test
    public void greetingTest_WithWorldAsArg() {
        assertEquals("Hello, World!", service.greeting("World"));
    }

    @Test
    public void greetingTest_WithMasterSplinterArg() {
        assertEquals("Hello, Master Splinter!", service.greeting("Master Splinter"));
    }
}