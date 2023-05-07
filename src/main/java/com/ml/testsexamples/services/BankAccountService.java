package com.ml.testsexamples.services;

import com.ml.testsexamples.dto.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankAccountService {

    private final BankAccountRepository repository;

    public List<BankAccount> findAll() {
        log.info("BankAccountService.getAll() - retrieving all bank accounts data.");
        return repository.findAll();
    }

    public Optional<BankAccount> findById(Long id) {
        log.info("BankAccountService.getById(id) - retrieving bank account data by id. id: {}", id);
        return repository.findById(id);
    }

    public Optional<BankAccount> update(Long id, List<Pair<BankAccountFields, String>> data) {
        log.info("BankAccountService.update(id) - update bank account data by id. id: {}, data: {}", id, data);
        Optional<BankAccount> original = repository.findById(id);
        if (original.isEmpty()) {
            return Optional.empty();
        } else {
            BankAccount.BankAccountBuilder customerDataBuilder = BankAccount.builder();
            BankAccount updated = customerDataBuilder
                    .id(original.get().getId())
                    .firstName(original.get().getFirstName())
                    .lastName(original.get().getLastName())
                    .balance(original.get().getBalance())
                    .minimumBalance(original.get().getMinimumBalance())
                    .active(original.get().isActive())
                    .build();

            for (Pair<BankAccountFields, String> pair : data) {
                switch (pair.getFirst()) {
                    case FIRST_NAME -> updated.setFirstName(pair.getSecond());
                    case LAST_NAME -> updated.setLastName(pair.getSecond());
                    case BALANCE -> updated.setBalance(BigDecimal.valueOf(Double.parseDouble(pair.getSecond())));
                    case MINIMUM_BALANCE -> updated.setMinimumBalance(BigDecimal.valueOf(Double.parseDouble(pair.getSecond())));
                    case ACTIVE -> updated.setActive(Boolean.getBoolean(pair.getSecond()));
                    default -> throw new IllegalArgumentException("You are unauthorized to update this field.");
                }
            }
            return Optional.of(repository.save(updated));
        }
    }
}