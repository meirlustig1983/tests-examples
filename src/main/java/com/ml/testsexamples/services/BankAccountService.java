package com.ml.testsexamples.services;

import com.ml.testsexamples.dto.CustomerDataDto;
import com.ml.testsexamples.enums.CustomerDataFields;
import com.ml.testsexamples.repositories.CustomerDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankAccountService {

    private final CustomerDataRepository repository;

    public List<CustomerDataDto> findAll() {
        log.info("BankAccountService.getAll() - retrieving all customers data.");
        return repository.findAll();
    }

    public Optional<CustomerDataDto> findById(Long id) {
        log.info("BankAccountService.getById(id) - retrieving customer data by id. id: {}", id);
        return repository.findById(id);
    }

    public Optional<CustomerDataDto> update(Long id, List<Pair<CustomerDataFields, String>> data) {
        log.info("BankAccountService.update(id) - update customer data by id. id: {}, data: {}", id, data);
        Optional<CustomerDataDto> original = repository.findById(id);
        if (original.isEmpty()) {
            return Optional.empty();
        } else {
            CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();
            CustomerDataDto updated = customerDataBuilder
                    .id(original.get().getId())
                    .firstName(original.get().getFirstName())
                    .lastName(original.get().getLastName())
                    .balance(original.get().getBalance())
                    .minimumBalance(original.get().getMinimumBalance())
                    .build();

            for (Pair<CustomerDataFields, String> pair : data) {
                switch (pair.getFirst()) {
                    case FIRST_NAME -> updated.setFirstName(pair.getSecond());
                    case LAST_NAME -> updated.setLastName(pair.getSecond());
                    case BALANCE -> updated.setBalance(Double.parseDouble(pair.getSecond()));
                    case MINIMUM_BALANCE -> updated.setMinimumBalance(Double.parseDouble(pair.getSecond()));
                    default -> throw new IllegalArgumentException("You are unauthorized to update this field.");
                }
            }
            return Optional.of(repository.save(updated));
        }
    }
}