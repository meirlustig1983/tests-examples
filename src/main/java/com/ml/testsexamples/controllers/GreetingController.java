package com.ml.testsexamples.controllers;

import com.ml.testsexamples.services.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/greeting")
public class GreetingController {

    private final GreetingService greetingService;

    @GetMapping
    public String defaultGreeting() {
        return greetingService.greeting();
    }

    @GetMapping("/{name}")
    public String customGreeting(@PathVariable String name) {
        return greetingService.greeting(name);
    }
}
