package com.ml.testsexamples.facades;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dao.Transaction;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.enums.TransactionType;
import com.ml.testsexamples.repositories.BankAccountRepository;
import com.ml.testsexamples.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class DataFacade {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;

    public List<BankAccount> findAllBankAccounts() {
        log.info("DataFacade.findAllBankAccounts() - retrieving all bank accounts data.");
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> findBankAccountByAccountId(String accountId) {
        log.info("DataFacade.findBankAccountByAccountId(accountId) - retrieving bank account data by accountId. accountId: {}", accountId);
        return bankAccountRepository.findBankAccountByAccountId(accountId);
    }

    public Optional<BankAccount> saveBankAccount(BankAccount bankAccount) {
        log.info("DataFacade.saveBankAccount(bankAccount) - save bank account. accountId: {}", bankAccount.getAccountId());
        return Optional.of(bankAccountRepository.save(bankAccount));
    }

    public void saveTransaction(Long bankAccountId, BigDecimal amount, TransactionType type) {
        log.info("DataFacade.saveTransaction(bankAccountId, amount, type) - save transaction. bankAccountId: {}, amount: {}, type: {}", bankAccountId, amount, type);
        Transaction.TransactionBuilder builder = Transaction.builder();
        transactionRepository.save(builder.bankAccountId(bankAccountId)
                .amount(amount)
                .type(type)
                .build());
    }

    public Optional<BankAccount> updateBankAccount(String accountId, List<Pair<BankAccountFields, String>> data) {
        log.info("DataFacade.updateBankAccount(accountId, data) - update bank account data by accountId an list of pairs. accountId: {}, data: {}", accountId, data);
        Optional<BankAccount> original = bankAccountRepository.findBankAccountByAccountId(accountId);
        if (original.isEmpty()) {
            return Optional.empty();
        } else {
            BankAccount.BankAccountBuilder builder = BankAccount.builder();
            BankAccount updated = builder
                    .id(original.get().getId())
                    .accountId(original.get().getAccountId())
                    .firstName(original.get().getFirstName())
                    .lastName(original.get().getLastName())
                    .balance(original.get().getBalance())
                    .minimumBalance(original.get().getMinimumBalance())
                    .active(original.get().isActive())
                    .createdAt(original.get().getCreatedAt())
                    .build();

            for (Pair<BankAccountFields, String> pair : data) {
                switch (pair.getFirst()) {
                    case FIRST_NAME -> updated.setFirstName(pair.getSecond());
                    case LAST_NAME -> updated.setLastName(pair.getSecond());
                    case BALANCE -> updated.setBalance(BigDecimal.valueOf(Double.parseDouble(pair.getSecond())));
                    case MINIMUM_BALANCE ->
                            updated.setMinimumBalance(BigDecimal.valueOf(Double.parseDouble(pair.getSecond())));
                    case ACTIVE -> updated.setActive(pair.getSecond().equalsIgnoreCase("true"));
                    default -> throw new IllegalArgumentException("You are unauthorized to update this field.");
                }
            }
            return Optional.of(bankAccountRepository.save(updated));
        }
    }

    public void deleteBankAccountByAccountId(String accountId) {
        log.info("DataFacade.deleteBankAccountByAccountId(accountId) - delete bank account by accountId accountId: {}", accountId);
        bankAccountRepository.deleteByAccountId(accountId);
    }
}