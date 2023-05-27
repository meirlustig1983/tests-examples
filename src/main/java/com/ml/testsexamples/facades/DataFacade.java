package com.ml.testsexamples.facades;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dao.Transaction;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.enums.TransactionType;
import com.ml.testsexamples.repositories.BankAccountRepository;
import com.ml.testsexamples.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class DataFacade {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public List<BankAccount> findAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> findBankAccountByAccountId(String accountId) {
        return bankAccountRepository.findBankAccountByAccountId(accountId);
    }

    public Optional<BankAccount> saveBankAccount(BankAccount bankAccount) {
        return Optional.of(bankAccountRepository.save(bankAccount));
    }

    public void saveTransaction(Long bankAccountId, BigDecimal amount, TransactionType type) {
        Transaction transaction = Transaction.builder()
                .bankAccountId(bankAccountId)
                .amount(amount)
                .type(type)
                .build();
        transactionRepository.save(transaction);
    }

    public Optional<BankAccount> updateBankAccount(String accountId, List<Pair<BankAccountFields, String>> data) {
        Optional<BankAccount> original = bankAccountRepository.findBankAccountByAccountId(accountId);
        return original.map(account -> {
            BankAccount.BankAccountBuilder builder = BankAccount.builder()
                    .id(account.getId())
                    .accountId(account.getAccountId())
                    .firstName(account.getFirstName())
                    .lastName(account.getLastName())
                    .balance(account.getBalance())
                    .minimumBalance(account.getMinimumBalance())
                    .active(account.isActive())
                    .createdAt(account.getCreatedAt());

            data.forEach(pair -> {
                BankAccountFields field = pair.getFirst();
                String value = pair.getSecond();

                switch (field) {
                    case FIRST_NAME -> builder.firstName(value);
                    case LAST_NAME -> builder.lastName(value);
                    case BALANCE -> builder.balance(new BigDecimal(value));
                    case MINIMUM_BALANCE -> builder.minimumBalance(new BigDecimal(value));
                    case ACTIVE -> builder.active(Boolean.parseBoolean(value));
                    default -> throw new IllegalArgumentException("You are unauthorized to update this field.");
                }
            });

            BankAccount updated = builder.build();
            return bankAccountRepository.save(updated);
        });
    }

    public void deleteBankAccountByAccountId(String accountId) {
        bankAccountRepository.deleteByAccountId(accountId);
    }
}
