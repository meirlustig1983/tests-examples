package com.ml.testsexamples.config;

import com.ml.testsexamples.mappers.BankAccountMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public BankAccountMapper bankAccountMapper() {
        return BankAccountMapper.INSTANCE;
    }
}
