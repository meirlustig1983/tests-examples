package com.ml.testsexamples.services;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GreetingServiceWithBeforeAndAfterTest {

    private GreetingService service;

    @BeforeAll
    public void init(){
        System.out.println("Start a new test class");
        service = new GreetingService();
    }

    @AfterAll
    public void end(){
        System.out.println("End of test class");
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