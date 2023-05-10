package com.ml.testsexamples.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.CONCURRENT)
@MockitoSettings
class GreetingServiceParallelExecutionTest {

    @InjectMocks
    private GreetingService service;

    @Test
    @DisplayName("Test a standard 'Hello, World!' greeting")
    @SneakyThrows
    public void greetingTest() throws Exception {
        Thread.sleep(1_000);
        assertEquals("Hello, World!", service.greeting());
    }

    @Test
    @DisplayName("Test a 'Hello, World!' greeting with argument")
    @SneakyThrows
    public void greetingTest_WithWorldAsArg() throws Exception {
        Thread.sleep(1_000);
        assertEquals("Hello, World!", service.greeting("World"));
    }

    @Test
    @DisplayName("Test a 'Hello, Master Splinter!' greeting with argument")
    @SneakyThrows
    public void greetingTest_WithMasterSplinterArg() throws Exception {
        Thread.sleep(1_000);
        assertEquals("Hello, Master Splinter!", service.greeting("Master Splinter"));
    }
}