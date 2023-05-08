package com.ml.testsexamples.managers;

import com.ml.testsexamples.dto.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
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

    public Optional<BankAccount> info(Long id) {
        log.info("BankManager.info(id) - get info about bank account. id: {}", id);
        Optional<BankAccount> bankAccount = service.findById(id);
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount;
    }

    public Optional<BankAccount> deposit(Long id, double amount) {
        log.info("BankManager.deposit(id,amount) - make a deposit to bank account. id: {}, amount: {}", id, amount);
        Optional<BankAccount> original = service.findById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return service.update(id, List.of(Pair.of(BankAccountFields.BALANCE, Double.toString(original.get().getBalance().doubleValue() + amount))));
    }

    public Optional<BankAccount> withdraw(Long id, double amount) {
        log.info("BankManager.withdraw(id,amount) - make a withdraw for bank account. id: {}, amount: {}", id, amount);
        Optional<BankAccount> original = service.findById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        if (original.get().getBalance().doubleValue() - amount < original.get().getMinimumBalance().doubleValue()) {
            throw new InsufficientFundsException("Insufficient funds exception");
        }
        return service.update(id, List.of(Pair.of(BankAccountFields.BALANCE, Double.toString(original.get().getBalance().doubleValue() - amount))));
    }
}
