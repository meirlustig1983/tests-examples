package com.ml.testsexamples.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
public class GreetingServiceTest {

    @InjectMocks
    private GreetingService service;

    @BeforeEach
    public void init(){
        System.out.println("Start a new test");
    }

    @AfterEach
    public void end(){
        System.out.println("End test");
    }

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