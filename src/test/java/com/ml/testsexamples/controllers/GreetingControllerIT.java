package com.ml.testsexamples.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class GreetingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void defaultGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/greeting"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"))
                .andDo(document("default-greeting"));
    }

    @Test
    void customGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/greeting/Meir"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Meir!"))
                .andDo(document("custom-greeting"));
    }
}