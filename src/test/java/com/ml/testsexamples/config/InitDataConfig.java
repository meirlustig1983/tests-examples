package com.ml.testsexamples.config;

import com.ml.testsexamples.dto.CustomerDataDto;
import com.ml.testsexamples.repositories.CustomerDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitDataConfig {

    @Bean
    public CommandLineRunner initData(CustomerDataRepository customerDataRepository) {
        return args -> {

            CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();

            CustomerDataDto customerData1 = customerDataBuilder
                    .firstName("Theodore")
                    .lastName("Roosevelt")
                    .balance(3500)
                    .minimumBalance(1500)
                    .build();

            CustomerDataDto customerData2 = customerDataBuilder
                    .firstName("Franklin")
                    .lastName("Benjamin")
                    .balance(0)
                    .minimumBalance(-1000)
                    .build();

            customerDataRepository.save(customerData1);
            customerDataRepository.save(customerData2);
        };
    }
}