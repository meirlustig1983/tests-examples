package com.ml.testsexamples.services;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.exceptions.InactiveAccountException;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import com.ml.testsexamples.facades.DataFacade;
import jakarta.persistence.EntityNotFoundException;
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

    private final DataFacade dataFacade;

    public Optional<BankAccount> getAccountInfo(Long id) {
        log.info("BankAccountService.getAccountInfo(id) - get info about bank account. id: {}", id);
        Optional<BankAccount> bankAccount = dataFacade.findBankAccountById(id);
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount;
    }

    public Optional<BankAccount> createAccount(BankAccount bankAccount) {
        log.info("BankAccountService.createAccount(bankAccount) - create bank account");
        return dataFacade.saveBankAccount(bankAccount);
    }

    public void deleteBankAccountById(Long id) {
        log.info("BankAccountService.deleteBankAccountById(id) - delete bank account. id: {}", id);
        dataFacade.deleteBankAccountById(id);
    }

    public Optional<BankAccount> activateAccount(Long id) {
        log.info("BankAccountService.activateAccount(id) - make a bank account active. id: {}", id);
        Optional<BankAccount> original = dataFacade.findBankAccountById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return dataFacade.updateBankAccount(id, List.of(Pair.of(BankAccountFields.ACTIVE, "true")));
    }

    public Optional<BankAccount> deactivateAccount(Long id) {
        log.info("BankAccountService.deactivateAccount(id) - make a bank account inactive. id: {}", id);
        Optional<BankAccount> original = dataFacade.findBankAccountById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return dataFacade.updateBankAccount(id, List.of(Pair.of(BankAccountFields.ACTIVE, "false")));
    }

    public Optional<BankAccount> makeDeposit(Long id, double amount) {
        log.info("BankAccountService.makeDeposit(id,amount) - make a deposit to bank account. id: {}, amount: {}", id, amount);
        Optional<BankAccount> original = dataFacade.findBankAccountById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (!original.get().isActive()) {
            throw new InactiveAccountException("Inactive bank account");
        }
        return dataFacade.updateBankAccount(id, List.of(Pair.of(BankAccountFields.BALANCE, Double.toString(original.get().getBalance().doubleValue() + amount))));
    }

    public Optional<BankAccount> makeWithdraw(Long id, double amount) {
        log.info("BankAccountService.makeWithdraw(id, amount) - make a withdraw for bank account. id: {}, amount: {}", id, amount);
        Optional<BankAccount> original = dataFacade.findBankAccountById(id);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (!original.get().isActive()) {
            throw new InactiveAccountException("Inactive bank account");
        } else if (original.get().getBalance().doubleValue() - amount < original.get().getMinimumBalance().doubleValue()) {
            throw new InsufficientFundsException("Insufficient funds exception");
        }
        return dataFacade.updateBankAccount(id, List.of(Pair.of(BankAccountFields.BALANCE, Double.toString(original.get().getBalance().doubleValue() - amount))));
    }
}