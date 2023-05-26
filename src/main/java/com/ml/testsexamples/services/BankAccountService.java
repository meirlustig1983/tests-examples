package com.ml.testsexamples.services;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dto.BankAccountDto;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.enums.TransactionType;
import com.ml.testsexamples.exceptions.AmountValidationException;
import com.ml.testsexamples.exceptions.EmailValidationException;
import com.ml.testsexamples.exceptions.InactiveAccountException;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import com.ml.testsexamples.facades.DataFacade;
import com.ml.testsexamples.mappers.BankAccountMapper;
import com.ml.testsexamples.validators.EmailValidator;
import jakarta.persistence.EntityNotFoundException;
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

    private final DataFacade dataFacade;

    private final BankAccountMapper mapper;

    public Optional<BankAccountDto> getAccountInfo(String accountId) {
        log.info("BankAccountService.getAccountInfo(accountId) - get info about bank account. accountId: {}", accountId);
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        }
        Optional<BankAccount> bankAccount = dataFacade.findBankAccountByAccountId(accountId);
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount.map(mapper::toDto);
    }

    public Optional<BankAccountDto> createAccount(BankAccountDto bankAccountDto) {
        log.info("BankAccountService.createAccount(bankAccount) - create bank account");
        return dataFacade.saveBankAccount(mapper.toDao(bankAccountDto)).map(mapper::toDto);
    }

    public void deleteBankAccountByAccountId(String accountId) {
        log.info("BankAccountService.deleteBankAccountByAccountId(accountId) - delete bank account. accountId: {}", accountId);
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        }
        dataFacade.deleteBankAccountByAccountId(accountId);
    }

    public Optional<BankAccountDto> activateAccount(String accountId) {
        log.info("BankAccountService.activateAccount(accountId) - make a bank account active. accountId: {}", accountId);
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        }
        Optional<BankAccount> original = dataFacade.findBankAccountByAccountId(accountId);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return dataFacade.updateBankAccount(accountId, List.of(Pair.of(BankAccountFields.ACTIVE, "true"))).map(mapper::toDto);
    }

    public Optional<BankAccountDto> deactivateAccount(String accountId) {
        log.info("BankAccountService.deactivateAccount(accountId) - make a bank account inactive. accountId: {}", accountId);
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        }
        Optional<BankAccount> original = dataFacade.findBankAccountByAccountId(accountId);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return dataFacade.updateBankAccount(accountId, List.of(Pair.of(BankAccountFields.ACTIVE, "false"))).map(mapper::toDto);
    }

    public Optional<BankAccountDto> makeDeposit(String accountId, double amount) {
        log.info("BankAccountService.makeDeposit(accountId,amount) - make a deposit to bank account. accountId: {}, amount: {}", accountId, amount);
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        } else if (amount < 0) {
            throw new AmountValidationException();
        }
        Optional<BankAccount> original = dataFacade.findBankAccountByAccountId(accountId);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (!original.get().isActive()) {
            throw new InactiveAccountException();
        }

        dataFacade.saveTransaction(original.get().getId(), BigDecimal.valueOf(amount), TransactionType.DEPOSIT);

        return dataFacade.updateBankAccount(accountId, List.of(Pair.of(BankAccountFields.BALANCE,
                Double.toString(original.get().getBalance().doubleValue() + amount)))).map(mapper::toDto);
    }

    public Optional<BankAccountDto> makeWithdraw(String accountId, double amount) {
        log.info("BankAccountService.makeWithdraw(id, amount) - make a withdraw for bank account. accountId: {}, amount: {}", accountId, amount);
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        } else if (amount < 0) {
            throw new AmountValidationException();
        }
        Optional<BankAccount> original = dataFacade.findBankAccountByAccountId(accountId);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (!original.get().isActive()) {
            throw new InactiveAccountException();
        } else if (original.get().getBalance().doubleValue() - amount < original.get().getMinimumBalance().doubleValue()) {
            throw new InsufficientFundsException();
        }

        dataFacade.saveTransaction(original.get().getId(), BigDecimal.valueOf(amount), TransactionType.WITHDRAW);

        return dataFacade.updateBankAccount(accountId, List.of(Pair.of(BankAccountFields.BALANCE,
                Double.toString(original.get().getBalance().doubleValue() - amount)))).map(mapper::toDto);
    }
}