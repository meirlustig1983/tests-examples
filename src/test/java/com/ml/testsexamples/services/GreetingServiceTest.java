package com.ml.testsexamples.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class GreetingServiceTest {

    @InjectMocks
    private GreetingService service;

    @Test
    @DisplayName("Test a standard 'Hello, World!' greeting")
    public void greetingTest() {
        assertEquals("Hello, World!", service.greeting());
    }

    @Test
    @DisplayName("Test a 'Hello, World!' greeting with argument")
    public void greetingTest_WithWorldAsArg() {
        assertEquals("Hello, World!", service.greeting("World"));
    }

    @Test
    @DisplayName("Test a 'Hello, Master Splinter!' greeting with argument")
    public void greetingTest_WithMasterSplinterArg() {
        assertEquals("Hello, Master Splinter!", service.greeting("Master Splinter"));
    }
}