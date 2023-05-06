package com.ml.testsexamples.managers;

import com.ml.testsexamples.dto.CustomerDataDto;
import com.ml.testsexamples.enums.CustomerDataFields;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import com.ml.testsexamples.services.BankAccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class BankManager {

    private final BankAccountService service;

    public Optional<CustomerDataDto> deposit(Long id, double amount) {
        log.info("BankManager.deposit(id,amount) - make a deposit to customer. id: {}, amount: {}", id, amount);
        Optional<CustomerDataDto> original = service.findById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid customer data ID");
        }
        return service.update(id, List.of(Pair.of(CustomerDataFields.BALANCE, Double.toString(original.get().getBalance() + amount))));
    }

    public Optional<CustomerDataDto> withdraw(Long id, double amount) {
        log.info("BankManager.withdraw(id,amount) - make a withdraw for customer. id: {}, amount: {}", id, amount);
        Optional<CustomerDataDto> original = service.findById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid customer data ID");
        }
        if (original.get().getBalance() - amount < original.get().getMinimumBalance()) {
            throw new InsufficientFundsException("Insufficient funds exception");
        }
        return service.update(id, List.of(Pair.of(CustomerDataFields.BALANCE, Double.toString(original.get().getBalance() - amount))));
    }
}
